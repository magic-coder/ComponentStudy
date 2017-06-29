package com.richfit.barcodesystemproduct.splash.imp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.richfit.barcodesystemproduct.BarcodeSystemApplication;
import com.richfit.barcodesystemproduct.splash.ISplashPresenter;
import com.richfit.barcodesystemproduct.splash.ISplashView;
import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.LocalFileUtil;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.db.BCSSQLiteHelper;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LoadBasicDataWrapper;
import com.richfit.domain.bean.LoadDataTask;
import com.richfit.domain.bean.MenuNode;
import com.richfit.domain.bean.UserEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by monday on 2016/12/2.
 */

public class SplashPresenterImp extends BasePresenter<ISplashView>
        implements ISplashPresenter {

    ISplashView mView;
    private int mTaskId = 0;
    private RxDownload mRxDownload;

    public SplashPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void onStart() {
        mTaskId = 0;
        mRxDownload = RxDownload.getInstance(mContext)
                .maxThread(1)
                .maxRetryCount(3);
    }

    /**
     * 下载基础数据的入口。
     * 注意这里由于系统必须去下载两种类型的ZZ的基础数据，
     * 然而二级单位的ZZ基础数据又比较特别(仅仅针对某些地区公司存在)，
     * 所以需要拦击错误，不管有没有都必须执行完所有的任务。
     *
     * @param requestParams
     */
    @Override
    public void loadAndSaveBasicData(final ArrayList<LoadBasicDataWrapper> requestParams) {
        mView = getView();
        ResourceSubscriber<Integer> subscriber = Flowable.fromIterable(requestParams)
                .concatMap(param ->
                        Flowable.just(param)
                                .zipWith(Flowable.just(mRepository.getLoadBasicDataTaskDate(param.queryType)), (loadBasicDataWrapper, queryDate) -> {
                                    loadBasicDataWrapper.queryDate = queryDate;
                                    return loadBasicDataWrapper;
                                }).flatMap(p -> mRepository.preparePageLoad(p)))
                .concatMap(param -> Flowable.fromIterable(addTask(param.queryType, param.queryDate, param.totalCount, param.isByPage)))
                .concatMapDelayError(task -> mRepository.loadBasicData(task))
                .flatMap(sourceMap -> mRepository.saveBasicData(sourceMap))
                .doOnComplete(() -> {
                    String currentDate = CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE4);
                    ArrayList<String> queryTypes = new ArrayList<>();
                    for (LoadBasicDataWrapper param : requestParams) {
                        queryTypes.add(param.queryType);
                    }
                    mRepository.saveLoadBasicDataTaskDate(currentDate, queryTypes);
                })
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<Integer>(mContext, "正在同步基础数据...") {
                    @Override
                    public void _onNext(Integer integer) {

                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        //网络超时
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_SYNC_BASIC_DATA_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mTaskId = 0;
                            mView.syncDataError(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mTaskId = 0;
                            mView.syncDataError(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mTaskId = 0;
                            mView.toLogin();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void downloadInitialDB() {
        mView = getView();
        if (SPrefUtil.sSPref == null) {
            SPrefUtil.initSharePreference(mContext.getApplicationContext());
        }
        boolean isAppFist = (boolean) SPrefUtil.getData(Global.IS_APP_FIRST_KEY, true);
        if (!isAppFist) {
            //如果不是第一次启动,那么直接同步基础数据
            mView.downDBComplete();
            return;
        }
        //如果是第一次启动那么下载数据库
        if (TextUtils.isEmpty(Global.MAC_ADDRESS)) {
            Global.MAC_ADDRESS = CommonUtil.getMacAddress();
        }
        final String dbName = BCSSQLiteHelper.DB_NAME;
        File fileDB = mContext.getDatabasePath(dbName);
        String dirDB = fileDB.getParent();
        if (fileDB.exists()) {
            fileDB.delete();
        }
        File file = new File(dirDB);
        if (!file.exists()) {
            file.mkdir();
        }
        final String savePath = file.getAbsolutePath();
        final String url = BarcodeSystemApplication.baseUrl + "downloadInitialDB?macAddress=" + Global.MAC_ADDRESS;
        L.e("下载基础数据库的url = " + url);

        ResourceObserver<DownloadStatus> observer = mRxDownload.download(url, dbName, savePath)
                .doOnComplete(() -> saveLoadBasicDataTaskDate())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceObserver<DownloadStatus>() {
                    @Override
                    public void onNext(DownloadStatus value) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.downDBFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.downDBComplete();
                        }
                    }
                });
        addSubscriber(observer);
    }

    @Override
    public void getConnectionStatus() {
        mView = getView();
        ResourceSubscriber<String> subscriber = mRepository.getConnectionStatus()
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.networkNotAvailable(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.networkAvailable();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void loadFragmentConfigs() {
        mView = getView();

        ResourceSubscriber<Boolean> subscriber = Flowable.just("fragment_configs.json")
                .map(name -> LocalFileUtil.getStringFormAsset(mContext, name))
                .map(json -> parseJson(json))
                .filter(list -> list != null && list.size() > 0)
                .map(list -> wrapperMenuNodes(list, Global.OFFLINE_MODE))
                .map(list -> mRepository.saveMenuInfo(list, "1", Global.OFFLINE_MODE))
                .map(list -> {
                    final UserEntity userEntity = new UserEntity();
                    userEntity.password = "1";
                    userEntity.loginId = "1";
                    userEntity.companyCode = "D740";
                    userEntity.companyId = "A8BB8253DDF56CADC46B3DB6BED48873";
                    userEntity.userId = "测试1";
                    userEntity.userId = "100060149671991351102";
                    userEntity.wmFlag = "N";
                    mRepository.saveUserInfo(userEntity);
                    return true;
                })
                .subscribeWith(new ResourceSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.toLogin();
                        }
                    }
                });
        addSubscriber(subscriber);

    }

    private ArrayList<MenuNode> wrapperMenuNodes(ArrayList<MenuNode> list, int mode) {
        ArrayList<MenuNode> menus = new ArrayList<>();
        if (list == null || list.size() == 0)
            return menus;
        for (MenuNode menuNode : list) {
            menuNode.setMode(mode);
            menus.add(menuNode);
        }
        return menus;
    }

    private ArrayList<MenuNode> parseJson(final String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        ArrayList<MenuNode> list =
                gson.fromJson(json, new TypeToken<ArrayList<MenuNode>>() {
                }.getType());
        return list;
    }


    private void saveLoadBasicDataTaskDate() {
        Log.d("yff", "下载基础DB完成;" + "saveLoadBasicDataTaskDate");
        final String currentDate = CommonUtil.getCurrentDate(Global.GLOBAL_DATE_PATTERN_TYPE4);
        ArrayList<String> queryTypes = new ArrayList<>();
        queryTypes.add("WL");
        queryTypes.add("CW");
        queryTypes.add("CC");
        queryTypes.add("XM");
        mRepository.saveLoadBasicDataTaskDate(currentDate, queryTypes);
    }

    /**
     * 生成下载任务。按照分页下载，每一页数据下载就是一个任务
     *
     * @param queryType
     * @param totalCount
     */
    private LinkedList<LoadDataTask> addTask(String queryType, String queryDate, int totalCount, boolean isByPage) {

        LinkedList<LoadDataTask> tasks = new LinkedList<>();

        if (!isByPage) {
            tasks.addLast(new LoadDataTask(++mTaskId, queryType, null, 0, 0, false, true));
        }

        if (totalCount == 0)
            return tasks;
        int count = totalCount / Global.MAX_PATCH_LENGTH;
        int residual = totalCount % Global.MAX_PATCH_LENGTH;
        int ptr = 0;
        if (count == 0) {
            // 说明数据长度小于PATCH_MAX_LENGTH，直接写入即可
            tasks.addLast(new LoadDataTask(++mTaskId, queryType, queryDate, "queryPage", 1
                    , totalCount, ptr, Global.MAX_PATCH_LENGTH, true, true));
        } else if (count > 0) {
            for (; ptr < count; ptr++) {
                tasks.addLast(new LoadDataTask(++mTaskId, queryType, queryDate, "queryPage",
                        Global.MAX_PATCH_LENGTH * ptr + 1, Global.MAX_PATCH_LENGTH * (ptr + 1),
                        ptr, Global.MAX_PATCH_LENGTH, true, ptr == 0 ? true : false));
            }
            if (residual > 0) {
                // 说明还有剩余的数据
                tasks.addLast(new LoadDataTask(++mTaskId, queryType, queryDate, "queryPage",
                        Global.MAX_PATCH_LENGTH * ptr + 1, Global.MAX_PATCH_LENGTH * ptr + residual,
                        ptr, Global.MAX_PATCH_LENGTH, true, false));
            }
        }
        return tasks;
    }
}

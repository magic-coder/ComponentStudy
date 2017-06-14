package com.richfit.data.repository;

import android.content.Context;

import com.richfit.data.db.BasicServiceDao;
import com.richfit.data.db.BusinessServiceDao;
import com.richfit.data.db.CheckServiceDao;
import com.richfit.data.db.InspectionServiceDao;
import com.richfit.data.db.ReferenceServiceDao;
import com.richfit.data.db.TransferServiceDao;
import com.richfit.data.net.api.IRequestApi;
import com.richfit.data.net.http.RetrofitModule;
import com.richfit.data.repository.local.LocalRepositoryImp;
import com.richfit.data.repository.server.ServerRepositoryImp;
import com.richfit.domain.repository.IBasicServiceDao;
import com.richfit.domain.repository.IBusinessService;
import com.richfit.domain.repository.ICheckServiceDao;
import com.richfit.domain.repository.IInspectionServiceDao;
import com.richfit.domain.repository.ILocalRepository;
import com.richfit.domain.repository.IReferenceServiceDao;
import com.richfit.domain.repository.IServerRepository;
import com.richfit.domain.repository.ITransferServiceDao;

/**
 * 初始化数据管理
 * Created by monday on 2017/6/9.
 */

public class DataInjection {

    private static Repository sInstance;

    public static Repository getRepository(Context context,String baseUrl) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = createRepository(context,baseUrl);
                }
            }
        }
        return sInstance;
    }

    public static void setRepository(Repository repository) {
        sInstance = repository;
    }


    private static IServerRepository getServerRepository(String baseUrl) {
        IRequestApi requestApi = RetrofitModule.getRequestApi(baseUrl);
        IServerRepository serverRepository = new ServerRepositoryImp(requestApi);
        return serverRepository;
    }

    private static ILocalRepository getLocalRepository(IBasicServiceDao basicServiceDao,
                                                       IInspectionServiceDao inspectionServiceDao,
                                                       IBusinessService businessServiceDao,
                                                       IReferenceServiceDao referenceServiceDao,
                                                       ITransferServiceDao transferServiceDao,
                                                       ICheckServiceDao checkServiceDao) {
        ILocalRepository localRepository = new LocalRepositoryImp(basicServiceDao, inspectionServiceDao, businessServiceDao,
                referenceServiceDao, transferServiceDao, checkServiceDao);
        return localRepository;
    }

    private static IBasicServiceDao getBasicServiceDao(Context context) {
        return new BasicServiceDao(context.getApplicationContext());
    }


    private static IInspectionServiceDao getInspectionServiceDao(Context context) {
        return new InspectionServiceDao(context);
    }

    private static IBusinessService getBusinessService(Context context) {
        return new BusinessServiceDao(context);
    }

    private static IReferenceServiceDao getReferenceServiceDao(Context context) {
        return new ReferenceServiceDao(context);
    }

    private static ITransferServiceDao getTransferServiceDao(Context context) {
        return new TransferServiceDao(context);
    }

    private static ICheckServiceDao getCheckServiceDao(Context context) {
        return new CheckServiceDao(context);
    }


    private static Repository createRepository(Context context, String baseUrl) {
        IBasicServiceDao basicServiceDao = getBasicServiceDao(context);
        IInspectionServiceDao inspectionServiceDao = getInspectionServiceDao(context);
        IBusinessService businessService = getBusinessService(context);
        IReferenceServiceDao referenceServiceDao = getReferenceServiceDao(context);
        ITransferServiceDao transferServiceDao = getTransferServiceDao(context);
        ICheckServiceDao checkServiceDao = getCheckServiceDao(context);
        ILocalRepository localRepository = getLocalRepository(basicServiceDao, inspectionServiceDao,
                businessService, referenceServiceDao, transferServiceDao, checkServiceDao);
        IServerRepository serverRepository = getServerRepository(baseUrl);
        return new Repository(serverRepository, localRepository);
    }

}

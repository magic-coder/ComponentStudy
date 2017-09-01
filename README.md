# android组件化实践项目

## 1. 项目主体构架基础
该项目的主体架构参考了clean-architecture项目，以及google的android-architecture项目。前期将RxJava，Dagger2,EventBus等融合到了该框架中，后续为了适应
组件化开发，去除了Dagger2框架。如果你想学习android开发的各大构架，这里有我看过的文章:<br/>

### 1.1 国内各大平台架构：

<ul>
<li><a href="http://www.uml.org.cn/mobiledev/201310211.asp" target="_blank">App工程结构搭建：几种常见Android代码架构分析</a></li>
<li><a href="http://www.infoq.com/cn/presentations/ctrip-mobile-architecture-evolution" target="_blank">携程Mobile架构演化(视频)</a></li>
<li><a href="http://www.infoq.com/cn/articles/ctrip-android-dynamic-loading" target="_blank">携程Android App插件化和动态加载实践</a></li>
<li><a href="http://www.infoq.com/cn/interviews/tj-taobao-client-arch" target="_blank">陶钧谈淘宝客户端应用框架实践</a></li>
<li><a href="http://www.infoq.com/cn/articles/alibaba-mobile-infrastructure" target="_blank">QCon旧金山演讲总结：阿里无线技术架构演进</a></li>
<li><a href="http://www.infoq.com/cn/news/2014/12/taobao-app-evolution" target="_blank">手机淘宝构架演化实践</a></li>
<li><a href="http://www.open-open.com/lib/view/open1436316754208.html" target="_blank">手机淘宝Android客户端架构</a></li>
<li><a href="http://club.alibabatech.org/resource_detail.htm?topicId=124" target="_blank">漫谈移动应用架构设计</a></li>
<li><a href="http://club.alibabatech.org/resource_detail.htm?topicId=130" target="_blank">大规模团队的Android开发</a></li>
<li><a href="http://club.alibabatech.org/resource_detail.htm?topicId=155" target="_blank">支付宝钱包客户端技术架构</a></li>
<li><a href="http://www.infoq.com/cn/presentations/mobile-baidu-android-platform-solutions" target="_blank">手机百度Android平台平台化解决方案</a></li>
<li><a href="http://www.infoq.com/cn/presentations/evolution-of-android-qq-music-architecture" target="_blank">涅盘新生—Android QQ音乐架构演进</a></li>
<li><a href="http://www.infoq.com/cn/articles/wechat-android-app-architecture" target="_blank">微信Android客户端架构演进之路</a></li>
<li><a href="https://mp.weixin.qq.com/s?__biz=MzAxNDUwMzU3Mw==&amp;mid=401044540&amp;idx=1&amp;sn=24b7d8fb655ae6dd5d989d0cb3c08e90&amp;scene=2&amp;srcid=0106EtxRjD2jHxzomxVPTwY3&amp;from=timeline&amp;isappinstalled=0&amp;uin=NzgwODIwNDgw&amp;key=&amp;devicetype=webwx&amp;version=70000001&amp;lang=zh_CN&amp;pass_ticket=46hW44w3Hxd7VY9rutz7mgLu1JGe2T1AAKNQpxNoYOSGi8NpmNYr+AZj+iXtRX2F" target="_blank">饿了么移动APP的架构演进</a></li>
</ul>

### 1.2 MVVM &amp; MVP &amp; MVC

<ul>
<li>
<a href="http://tech.vg.no/2015/07/17/android-databinding-goodbye-presenter-hello-viewmodel/" target="_blank">ANDROID DATABINDING: GOODBYE PRESENTER, HELLO VIEWMODEL</a><br>（viewmodel，安卓中的databinding）</li>
<li>
<a href="http://www.codeproject.com/Articles/166952/MVVM-in-Android" target="_blank">MVVM-in-Android</a>（android中的mvvm）</li>
<li>
<a href="https://github.com/CameloeAnthony/ZhiHuMVP" target="_blank"> ZhiHuMVP github 地址</a>（MVP架构思想，Retrofit RESTful API 框架的配合，RxJava 响应式编程）</li>
<li>
<a href="https://github.com/antoniolg/androidmvp" target="_blank"> androidmvp github地址</a>（star2000+的MVP实例）</li>
<li>
<a href="http://antonioleiva.com/mvp-android/" target="_blank">MVP for Android: how to organize the presentation layer</a>（上面这个github对应的文章）</li>
<li>
<a href="https://github.com/konmik/konmik.github.io/wiki/Introduction-to-Model-View-Presenter-on-Android" target="_blank"> Introduction-to-Model-View-Presenter-on-Android</a>（MVP的介绍，MVP必读经典）</li>
<li><a href="http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0425/2782.html" target="_blank">Introduction-to-Model-View-Presenter-on-Android 中文翻译版</a></li>
<li>
<a href="https://github.com/spengilley/ActivityFragmentMVP" target="_blank">ActivityFragmentMVP github地址</a>（MVP处理Activity和Fragment，使用了Dagger 注入）</li>
<li>
<a href="https://github.com/pedrovgs/EffectiveAndroidUI" target="_blank"> EffectiveAndroidUI github地址</a>（star 3000+的mvp，mvvm实例）</li>
<li>
<a href="https://github.com/glomadrian/MvpCleanArchitecture" target="_blank"> MvpCleanArchitecture github地址</a>（使用clean architecture 和mvp的实例）</li>
<li>
<a href="https://github.com/saulmm/Material-Movies" target="_blank"> Material-Movies github地址</a>（ 使用material design +MVP实现的Material-Movies）</li>
<li>
<a href="https://github.com/rallat/EffectiveAndroid" target="_blank">EffectiveAndroid github地址</a>（MVP+clean Architecture 项目）</li>
<li>
<a href="https://github.com/CameloeAnthony/AndroidMVPDemo" target="_blank">AndroidMVPDemo github地址</a>（本文作者MVP demo github地址）</li>
<li>
<a href="http://willowtreeapps.com/blog/mvvm-on-android-what-you-need-to-know/" target="_blank">MVVM on Android: What You Need to Know</a>(MVVM介绍，这个博客很不错)</li>
<li>
<a href="https://developer.android.com/tools/data-binding/guide.html" target="_blank">data-bingding guide</a>（data-binding guide官网）</li>
<li>
<a href="http://www.cnblogs.com/dxy1982/p/3793895.html" target="_blank">Web开发的MVVM模式</a>（MVC VS. MVP VS. MVVM）</li>
<li><a href="http://www.liuguangli.win/archives/299" target="_blank">Android应用开发架构概述</a></li>
<li>
<a href="http://objccn.io/issue-13-1/" target="_blank">MVVM介绍</a>（iOS中MVVM的一种实现，对概念的理解有帮助）</li>
</ul>

### 1.3 Clean Architecture

<ul>
<li>
<a href="https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html" target="_blank">The Clean Architecture</a>(clean architecture出处)</li>
<li>
<a href="https://github.com/android10/Android-CleanArchitecture" target="_blank">Android-CleanArchitecture github地址</a>（The Clean Architecture文章的例子）</li>
<li>
<a href="https://medium.com/ribot-labs/android-application-architecture-8b6e34acda65#.b29vhtdm2" target="_blank">Android Application Architecture原文</a> </li>
<li><a href="http://www.jianshu.com/p/8ca27934c6e6" target="_blank">Android Application Architecture中文翻译</a></li>
<li><a href="http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/" target="_blank">Architecting Android…The evolution</a></li>
<li><a href="http://www.devtf.cn/?p=1083" target="_blank">Architecting Android…The evolution中文翻译</a></li>
</ul>

### 1.4 其它

<ul>
<li><a href="https://github.com/Juude/Awesome-Android-Architecture/blob/master/Library.md" target="_blank">Artchitecture Library</a></li>
<li><a href="https://plus.google.com/+AndroidDevelopers/posts/3C4GPowmWLb" target="_blank">Design for Offline: Android App Architecture Best Practices</a></li>
<li><a href="http://blog.joanzapata.com/robust-architecture-for-an-android-app/" target="_blank">Robust and readable architecture for an Android App</a></li>
<li><a href="https://events.google.com/io2015/schedule?sid=358c9f91-b6d4-e411-b87f-00155d5066d7#day1/358c9f91-b6d4-e411-b87f-00155d5066d7" target="_blank">Android application architecture</a></li>
<li>
<a href="http://mp.weixin.qq.com/s?__biz=MzA3ODg4MDk0Ng==&amp;mid=403539764&amp;idx=1&amp;sn=d30d89e6848a8e13d4da0f5639100e5f#rd" target="_blank">google官方MVP架构示例项目解析</a>（google官网架构中文解析）</li>
<li>
<a href="https://github.com/jiangqqlmj" target="_blank">jiangqqlmj</a>/<strong><a href="https://github.com/jiangqqlmj/FastDev4Android" target="_blank">FastDev4Android</a></strong> （android快速开发框架）</li>
<li><a href="https://www.zhihu.com/question/21406685" target="_blank">知乎：Android 开发有什么好的架构么?</a></li>
<li>
<a href="https://www.zhihu.com/question/28564947" target="_blank">知乎：如果从0创建一个Android APP，设计思路是什么？（架构、activity、layout等复用性的考虑），感觉无从下手</a>
</li>
</ul>
<br/>
<br/>经过大量的实践，结合我们项目的需求，我选择了mvp业务的基本构架。我个人认为各个各样的框架其实很多，你需要自己去体会它们，然后通过动手实践后改造以后框架，融合自己的业务，最终形成自己的可行性构架。这里有系列文章，我觉得这些开发者也是在阅读和大量实践下，不断拥抱新的技术革新，提出自己的解决方案:<br/>

> 1. http://blog.csdn.net/marktheone/article/details/54891373
> 2. http://www.jianshu.com/p/8ca27934c6e6

## 2. ComponentStudy需要解决的问题
我们的业务是开发一套app，给60几家公司提供仓储管理。从业务来看每一家公司的业务或多或少都存在区别。通过前期的大量调研，以及参考之前的开发经验，逐步形成了9套左右的标准功能。所以我们考虑的是能不能将每一个标准功能做成一个组件。通过gradle动态组快速组合一个module，该module是一个最终形成的是一个插件，而在启动App后，通过配置文件启动不同的module，从而满足不同公司的需求。而且能够完全避免不同公司也就是不同module之间的数据污染。

## 3. 进一步思考的问题

* 1.首先我们需要考虑资源的冲突问题，因为不同module的客户化需要引入一定的资源，所以module可能会与组件和公共lib的资源冲突。我们知道在现在流行的插件开发中，如何解决宿主和插件之间的资源访问问题是也是一个棘手的问题(具体请看后续的small插件化框架源码分析)。而组件化可以通过build.gradle通过设置resourcePrefix来避免component的资源冲突，然后我们将标志公共的资源文件写入公共lib_style,lib_resource等基础库中。当module需要客户化资源时，需要结合module的resourcePrefix引入资源。<br/>

* 2.其次是怎么将MVP模式引入到每一个component,然后就是尽量考虑业务的扩展性。这里我们参考spring的AOP思想，也就是说我们让每一个标准component实现相同接口，如果module需要客户化，动态代理标准接口方法，这样我们能够轻松的修饰我们的标准功能。











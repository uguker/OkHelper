# OkUtils
OkGo二次封装，地址：https://github.com/jeasonlzy/okhttp-OkGo。
应用于简单日常接口调用。没有实现的功能请使用OkGo，具体使用请查看源码。
## 导入
1. 在build.gradle添加如下代码：<br>
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2. 添加依赖关系
```
dependencies {
	implementation 'com.github.uguker:okutils:1.2.0'
}
```
## 简单使用

1.2.0版本重构了Api,优化代码，与之前的不通用

1.初始化
```
    OkUtils.getInstance()
                .init(this)             // Application
                .openDebug()            // 有多个参数
                .setFailedCode(101)     // 设置默认失败码
                .setSucceedCode(200)    // 设置默认成功码
                .setResponseClass(ResponseImpl.class)   // 设置解析数据接口实现体
                .addCommonHeaders(headers)   // 添加通用Headers
                .addCommonParams(params)   // 添加通用Params
                // 通用数据转换，返回值转为返回实体前是使用的Json数据
                .setConvertHandler(new ConvertHandler() {
                    @Override
                    public String onHandle(String body) {
                        return body;
                    }
                })
                // 通用加密处理。对参数进行加密
                .setEncryptHandler(new EncryptHandler() {
                    @Override
                    public HttpParams onHandle(HttpParams params) {
                        return params;
                    }
                })
                // 拦截指定的headers
                .setHeadersInterceptor(new HeadersInterceptor() {
                    @Override
                    public boolean onIntercept(Headers headers) {
                        return false;
                    }
                })
                // 拦截指定Response
                .setResponseInterceptor(new ResponseInterceptor() {
                    @Override
                    public boolean onIntercept(Response response) {
                        return false;
                    }
                })
                // 设置加载对话框。可以通过实现Loading接口继承DialogFragment自定义。
                .setLoading(new LoadingDialog().duration(500).dimEnabled(true));
```
2.使用

```
    OkUtils
                .toObj(this)        // 返回Response<Object>实体，传入this，用来防止异步空指针
                //.toStr(this)        // 返回Response<String>实体
                //.toObj(this, String.class)      // 返回Response<String>实体
                //.toInitial(this)                // 返回Response<String>实体，String为接口返回原始数据
                //.toList(this, String.class)     // 返回Response<List<String>>实体
                .get(url)           // 请求方式，包括post、delete、put等
                .tag(tag)           // 请求Tag,用于取消
                .params("1", "1")   // 传参
                .multipart()    // 强制表单，对get、head、trace无效
                .spliceUrl()    // 强制拼接，对get、head、trace无效
                .upJson(json)       // 上传json数据,，对get、head、trace无效
                .upString(string)   // 上传文本数据,，对get、head、trace无效
                .succeedCodes(0, 200) // code 2、200在onSucceed中回调
                .failedCodes(10, 201) // code 2、200在onFailed中回调
                ...                 // 更多请自行摸索
                .loading(this)      // 显示加载对话框
                .loadingColors(Color.BLACK) // 颜色有两种状态,一个颜色为一种加载动画，多种颜色为另一种动画
                .loadingSize(size)          // 加载对话框大小
                .loadingDimEnabled(enable)  // 是否显示遮罩
                //.execute();       // 同步。所有ResponseInterceptor和HeadersInterceptor无效
                .execute(new Callback<Response<Object>>() {
                    @Override
                    public void onSucceed(Response<Object> response) {
                        // 成功回调
                    }

                    @Override
                    public void onFailed(Response<Object> response) {
                        // 失败回调
                    }
                });

                OkUtils.showLoading(activity);  // 显示加载
                OkUtils.dismissLoading(activity);  // 取消加载
               // 更多请自行摸索或查看源代码
```


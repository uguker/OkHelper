# OkHelper
OkGo二次封装，应用于简单日常接口调用。
具体使用请查看Demo和源码
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
	implementation 'com.github.uguker:okutils:2.0.0'
}
```
## 简单使用
2.0.0重构Api,优化代码

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







历史版本1.1.1使用
```
	OkUtils.init(app);
	OkUtils
	        // 返回NetData<File>实体
	        // .toFile()
		    // 返回NetData<List<Object>>实体
		    .toList(Object.class)
		    // 返回NetData<Object>实体
		    // .toObj(Object.class)
		    // .toObj()
		    // .toStr()
		    // code为11111,1,2的数据将调用onSucceed
		    //.filters(11111,1,2)
		    .filters(true, 11111,1,2)
		    // code为11111,1,2的数据将调用onFailed
            	    //.filters(false, 11111,1,2)
		    // 标签
		    .tag(this)
		    // 加上这句会显示Loading
		    .loading(context)
		    // Loading颜色
		    .loadingColor(Color.RED)
		    // 文字
		    .loadingText("正在加载...")
		    // 进度条大小
		    .loadingSize(60)
		    // 背景是否昏暗
		    .loadingDimEnabled(true)
		    // 方法
		    .post(url)
		    // 传参
		    // .params()
		    // .params()
		    .execute(new BaseCallback<NetData<List<Object>>>(this) {
		        @Override
		        public void onSuccess(NetData<List<Object>> listNetData) {

		        }

		        @Override
		        public void onError(String msg) {

		        }
		    });
```


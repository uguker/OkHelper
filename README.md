# OkHelper
OkGo二次封装，应用于简单日常接口调用。

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
	implementation 'com.github.uguker:okhelper:1.0.3'
}
```
## 简单使用
```
	OkHelper.init(app);
	OkHelper
	        // 返回NetData<File>实体
	        // .toFile()
		    // 返回NetData<List<Object>>实体
		    .toList(Object.class)
		    // 返回NetData<Object>实体
		    // .toObj(Object.class)
		    // .toObj()
		    // .toStr()
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


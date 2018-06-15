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
	implementation 'com.github.uguker:OkHelper:1.0.1'
}
```
## 简单使用
```
	OkHelper.init(app);
	OkHelper
        // 返回NetBean<List<Object>>实体
        .toList(Object.class)
        // 返回NetBean<Object>实体
        //.toObj(Object.class)
        // 加上这句会显示Loading
        .with(this)
        // Loading颜色
        .loadingColor(Color.RED)
        // 文字
        .loadingText("正在加载...")
        // 背景是否昏暗
        .dimEnabled(true)
        // 方法
        .post(url)
        // 传参
        //.params()
        //.params()
        .execute(new BaseCallback<NetBean<List<Object>>>(this) {
            @Override
            public void onSuccess(NetBean<List<Object>> listNetBean) {

            }

            @Override
            public void onError(String msg) {

            }
        });
```


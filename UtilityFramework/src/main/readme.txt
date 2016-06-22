v4 1.6+
v7 2.1+
v13 3.2+

怎么简单怎么搞，不用太注重较为复杂的封装！

已完成：
webapp的bestvframework
用户中心





混淆时注意事项：

    添加Android默认混淆配置${sdk.dir}/tools/proguard/proguard-android.txt
    不要混淆xUtils中的注解类型，添加混淆配置：-keep class * extends java.lang.annotation.Annotation { *; }
    对使用DbUtils模块持久化的实体类不要混淆，或者注解所有表和列名称@Table(name="xxx")，@Id(column="xxx")，@Column(column="xxx"),@Foreign(column="xxx",foreign="xxx")；






收集、反编译：
1、友盟更新、分享、统计、推送 http://www.umeng.com
2、sharesdk

webwalker.framework.widget 控件类
webwalker.framework.prototype 系统原生类的封装

组合其他开源框架
组合我的毕业设计

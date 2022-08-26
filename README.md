![](https://github.com/pengxurui/AndroidFamily/blob/master/images/Android_Banner.png)
  
<p align='center'>
  <a>
    <img src="https://www.jitpack.io/v/pengxurui/EasyTrack.svg">
  </a>
  <a href="https://www.github.com/pengxurui" target="_blank">
    <img src="https://komarev.com/ghpvc/?username=pengxurui&style=flat&label=👁%20Views">
  </a>
</p>


<p align='center'>
  <a href="https://www.github.com/pengxurui" target="_blank">
    <img src="https://img.shields.io/badge/作者-@小彭-brightgreen.svg?style=flat&logo=GitHub">
  </a>
  <a href="https://github.com/pengxurui/Android-NoteBook/raw/master/images/搜一搜公众号.png" target="_blank">
    <img src="https://img.shields.io/badge/公众号-彭旭锐-brightgreen.svg?style=flat&logo=WeChat">
  </a>
  <a href="https://juejin.cn/user/1063982987230392" target="_blank">
    <img src="https://img.shields.io/badge/🔥%20juejin-掘金-blue.svg?style=flat">
  </a>
  <a href="https://www.zhihu.com/people/pengxurui" target="_blank">
    <img src="https://img.shields.io/badge/zhihu-知乎-informational.svg?style=flat&logo=Zhihu">
  </a>
  <a href="https://www.toutiao.com/c/user/token/MS4wLjABAAAAbY-k9r66YVymjlqMaaGZJO67hCNYaNGa7PCoisQYmR4" target="_blank">
    <img src="https://img.shields.io/badge/toutiao-头条-red.svg?style=flat">
  </a>
  <a href="https://www.cnblogs.com/pengxurui/" target="_blank">
    <img src="https://img.shields.io/badge/cnblogs-博客园-green.svg?style=flat">
  </a>
  <a href="https://blog.csdn.net/pengxurui?spm=1000.2115.3001.5343" target="_blank">
    <img src="https://img.shields.io/badge/csdn-CSDN-9cf.svg?style=flat">
  </a>
  <a href="" target="_blank">
    <img src="https://img.shields.io/badge/jianshu-简书-orange.svg?style=flat">
  </a>
</p>


# 前言

- 目前，几乎每个商用应用都有数据埋点的需求。你的 App 是怎么做埋点的呢，有遇到让你 “难顶” 的问题吗？
- 在这篇文章里，我将带你建立数据埋点的基本认识，还会介绍西瓜视频团队的前端埋点方案，最后为你带来我的落地实现 EasyTrack。

---
# 目录

![](https://github.com/pengxurui/EasyTrack/blob/master/images/EasyTrack%20-%20%E7%9B%AE%E5%BD%95.png)

---
# 1. 数据埋点概述

#### 1.1 为什么要埋点？

“除了上帝，任何人都必须用数据说话”，在数据时代，使用数据驱动产品迭代已经称为行业共识。在分析应用数据之前，首先需要获得数据，这就需要前端或服务端进行数据埋点。

#### 1.2 数据需求的工作流程

首先，你需要了解数据需求的工作流程，需求是如何产生，又是如何流转的，主要分为以下几个环节：

- **1、需求产生：** 产品需求引起产品形态变化，产生新的数据需求；
- **2、事件设计：** 数据产品设计埋点事件并更新数据字典文档，提出埋点评审；
- **3、埋点开发：** 开发进行数据埋点开发；
- **4、埋点测试：** 测试进行数据埋点测试，确保数据质量；
- **5、数据消费：** 数据分析师进行数据分析，推荐系统工程师进行模型训练，赋能产品运营决策。

![](https://github.com/pengxurui/EasyTrack/blob/master/images/EasyTrack%20-%20%E6%B5%81%E7%A8%8B.png)

#### 1.3 数据消费的经典场景

|消费场景|需求描述|技术需求|
|:---|:---|:---|
|**渗透率分析**|统计 DAU/PV/UV/VV 等|准确的上报时机|
|**归因分析**|分析前因后果|准确上报上下文 (如场景、会话、来源页面)|
|**1. A / B 测试<br>2. 个性化推荐**|分析用户特征、产品特征等|准确上报事件属性| 

可以看到，在归因分析中，除了需要上报事件本身的属性之外，还需要上报事件产生时的上下文信息，例如当前页面、来源页面、会话等。

#### 1.4 埋点数据采集的基本模型

数据采集是指在前端或服务端收集需要上报的事件属性的过程。为了满足复杂、高效的数据消费需求，需要科学合理地设计端侧的数据采集逻辑，基本可以总结为 “4W + 1H” 模型：

|模型|描述|举例|
|:---|:---|:---|
|**1、WHAT**|什么行为|事件名|
|**2、WHEN**|行为产生的时间|时间戳|
|**3、WHO** |行为产生的对象|对象唯一标识 (例如用户 ID、设备 ID)|
|**4、WHERE**|行为产生的环境|设备所处的环境 (例如 IP、操作系统、网络)|
|**5、HOW**|行为的特征|上下文信息 (例如当前页面、来源页面、会话)|

---
# 2. 如何实现数据埋点？

#### 2.1 埋点方案总结

目前，业界已经存在多种埋点方案，主要分为全埋点、前端代码埋点和服务端代码埋点三种，优缺点和适用场景总结如下：

||全埋点|前端埋点|服务端埋点|
|:---|:---|:---|:---|
|优势|开发成本低|完整采集上下文信息|不依赖于前端版本|
|劣势|数据量大，无法获取上下文数据，数据质量低|前端开发成本较高|服务端开发成本较高、获取上下文信息依赖于接口传值|
|适用场景|通用基础事件（如启动/退出、浏览、点击）|核心业务流程（如登录、注册、收藏、购买）|核心业务结果事件（如支付成功）|

- **1、全埋点：** 指通过编译时插桩、运行时动态代理等 AOP 手段实现自动埋点和上报，无须开发者手动进行埋点，因此也称为 “无埋点”；

- **2、前端埋点：** 指前端 (包括客户端) 开发者手动编码实现埋点，虽然可以通过埋点工具或者脚本简化埋点开发工作，但总体上还是需要手动操作；

- **3、服务端埋点：** 指服务端手动编码实现埋点，缺点是需要客户端需要侵入接口来保留上下文参数。

#### 2.2 全埋点方案的局限性

表面上看，全埋点方案的优势很明显：客户端和服务端只需要一次开发，就能实现所有页面、所有路径的曝光和点击事件埋点，节省了研发人力，也不用担心埋点逻辑会侵入正常业务逻辑。然而，不可能存在完美的解决方案，全埋点方案还是存在一些局限性：

- **1、资源消耗较大：** 全场景上报会产生大量无用数据，网络传输、数据存储和数据计算需要消耗大量资源；

- **2、页面稳定性要求较高：** 需要保持页面视图结构相对稳定，一旦页面视图结果变化，历史录入的埋点数据就会失效；

- **3、无法采集上下文信息：** 无法采集事件产生时的上下文信息，也就无法满足复杂的数据消费需求。

#### 2.3 埋点设计的整体方案

考虑的不同方案都存在优缺点，单纯采用一种埋点方案是不切实际的，需要根据不同业务场景和不同数据消费需要而采用不同的埋点方案：

- **1、全埋点：** 作为全局兜底方案，可以满足粗粒度的统计需求；

- **2、前端埋点：** 作为全埋点的补充方案，可以自定义埋点参数，主要处理核心业务流程事件，例如（如登录、注册、收藏、购买）；

- **3、服务端埋点：** 核心业务结果事件，例如订单支付成功。

---
# 3. 前端埋点中的困难

#### 3.1 一个简单的埋点场景

现在，我们通过一个具体的埋点场景，试着发现在做埋点需求时会遇到的困难或痛点。我直接使用西瓜视频中的一个埋点场景：

![](https://github.com/pengxurui/EasyTrack/blob/master/images/EasyTrack%20-%20%E5%9B%B0%E9%9A%BE.png)

—— 图片引用自西瓜视频技术博客

这个产品场景很简单，左边是西瓜视频的推荐流列表，点击 “电影卡片” 会进入右边的 “电影详情页” 。**两个页面中都有 “收藏按钮”，现在的数据需求是采集不同页面中 “收藏按钮” 的点击事件，以便分析用户收藏影片的行为，优化影片的推荐模型。**

- **1、在推荐列表页中上报点击事件：**
```
“event_name" : "click_favorite", // 事件名
"cur_page" : "feed",             // 当前页面
"video_id" : "123",              // 影片 ID
"video_name" : "影片名",          // 影片名
"video_type" : "1",              // 影片类型
"$user_id" : "10000",            // 用户 ID
"$device_id" : "abc"             // 设备 ID
...                              // 其他预置属性
```

- **2、在电影详情页中上报点击事件：**
```
“event_name" : "click_favorite", // 事件名
"from_page" : "feed"
"cur_page" : "video_detail",     // 当前页面
"video_id" : "123",              // 影片 ID
"video_name" : "影片名",          // 影片名
"video_type" : "1",              // 影片类型
"$user_id" : "10000",            // 用户 ID
"$device_id" : "abc"             // 设备 ID
...                              // 其他预置属性
```

#### 3.2 现状分析

理解了这个埋点场景之后，我们先梳理出目前遇到的困难：

- **1、埋点参数分散：** 需要上报的埋点参数位于不同 UI 容器或不同业务模块，代码跨度很大（例如：Activity、Fragment、ViewHolder、自定义 View）；

- **2、组件复用：** 组件抽象复用后在多个页面使用（例如通用的 ViewHolder 或自定义 View)；

- **3、数据模型不一致：** 不同场景 / 页面下描述状态的数据模型不一致，需要额外的转换适配过程（例如有的模型用 video_type 表示影片类型，另一些模型用 videoType 表示影片类型）。

#### 3.3 评估标准

理解了问题和现状，现在我们开始尝试找到解决方案。为此，我们需要想清楚理想中的解决方案，应该满足什么标准：

- **1、准确性：** 这是核心目标，能够在保证不同场景 / 页面下准确收集埋点数据；
- **2、简洁性：** 使用方法尽可能简单，收敛模板代码；
- **3、可用性：** 尽可能高效稳定，不容易出错，性能开销小。

#### 3.4 常规解决方案

**1、逐级传递 —— 通过面向对象的关系逐级传递埋点参数：**

通过 Android 框架支持的 Activity / Fragment 参数传递方式和面向对象程序设计，逐级将埋点参数传递到最深层的收藏按钮。例如：

- **列表页：** Activity -> ViewModel -> FeedFragment (推荐) -> Adapter -> ViewHolder (电影卡片) -> CollectButton (收藏按钮)

- **详情页：** Activity -> ViewModel -> DetailBottomFragment（底部功能区） -> CollectButton (收藏按钮)

缺点 (参数传递困难) ：传递数据需要编写大量重复模板代码，工程代码膨胀，增大维护难度。再叠加上组件复用的情况，逐级传递会让代码复杂度非常高，很明显不是一个合理的解决方案。

**2、Bean 传递 —— 在 Java Bean 中增加字段来收集埋点参数：**

缺点 (违背单一职责原则)：Java Bean 中侵入了与业务无关的埋点参数，同时会造成 Java Bean 数据冗余，增大维护难度。

**3、全局单例 —— 通过全局单例对象来收集埋点参数：**

这个方案与 “Bean 传递 ” 类似，区别在于埋点参数从 Java Bean 中移动到全局单例中，但缺点还是很明显：

缺点 (写入和清理时机)：单例会被多个位置写入，一旦被覆盖就无法被恢复，容易导致上报错误；另外清理的时机也难以把握，清理过早会导致埋点参数丢失，清理过晚会污染后面的埋点事件。

---
# 4. 西瓜视频方案

理解了数据埋点开发中的困难，有没有什么方案可以简化埋点过程中的复杂度呢？我们来讨论下西瓜视频团队分享的一个思路：**基于视图树收集埋点参数。**

![](https://github.com/pengxurui/EasyTrack/blob/master/images/EasyTrack%20-%20%E8%A5%BF%E7%93%9C1.png)

![](https://github.com/pengxurui/EasyTrack/blob/master/images/EasyTrack%20-%20%E8%A5%BF%E7%93%9C2.png)

—— 图片引用自西瓜视频技术博客

通过分析数据与视图节点的关系可以发现，事件的埋点数据正好分布在视图树的不同节点中。当 “收藏按钮” 触发事件时，只需要沿着视图树逐级向上查找 (通过 View#getParent()) 就可以收集到所有数据。

并且，树的分支天然地支持为参数设置不同的值。例如 “推荐 Fragment” 需要上报 “channel : recomment”，而 “电影 Fragment” 需要上报 “channel : film”。因为 Fragment 的根布局对应有视图树中的不同节点，所以在不同 Fragment 中触发的事件最终收集到的 “channel” 参数值也就不同了。Nice~

---
# 5. EasyTrack 埋点框架

思路 Get 到了，现在我们来讨论如何应用这个思路来解决问题。贴心的我已经帮你实现为一个框架 EasyTrack。源码地址：https://github.com/pengxurui/EasyTrack

#### 5.1 添加依赖

- **1、依赖 JitPack 仓库**

在项目级 build.gradle 声明远程仓库：
```
allprojects {
    repositories {
        google()
        mavenCentral()
        // JitPack 仓库
        maven { url "https://jitpack.io" }
    }
}
```
- **2、依赖 EasyTrack 框架**

在模块级 build.gradle 中依赖类库：
```
dependencies {
    ...
    // 依赖 EasyTrack 框架
    implementation 'com.github.pengxurui:EasyTrack:v1.0.1'
    // 依赖 Kotlin 工具（非必须）
    implementation 'com.github.pengxurui:KotlinUtil:1.0.1'
}
```

#### 5.2 依附埋点参数到视图树

`ITrackModel`接口定义了一个数据填充能力，你可以创建它的实现类来定义一个数据节点，并在 fillTrackParams() 方法中声明参数。例如：MyGoodsViewHolder 实现了 ITrackMode 接口，在 fillTrackParams() 方法中声明参数（goods_id / goods_name）。

随后，通过 View 的扩展函数`View.trackModel()`将其依附到视图节点上。扩展函数 View.trackModel() 内部基于 View#setTag() 实现。

`MyGoodsViewHolder.kt`
```
class MyGoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ITrackModel {

    private var mItem: GoodsItem? = null

    init {
        // Java：EasyTrackUtilsKt.setTrackModel(itemView, this);
        itemView.trackModel = this
    }

     override fun fillTrackParams(params: TrackParams) {
        mItem?.let {
            params.setIfNull("goods_id", it.id)
            params.setIfNull("goods_name", it.goods_name)
        }
    }
}
```

`EasyTrackUtils.kt`
```
/**
 * Attach track model on the view.
 */
var View.trackModel: ITrackModel?
    get() = this.getTag(R.id.tag_id_track_model) as? ITrackModel
    set(value) {
        this.setTag(R.id.tag_id_track_model, value)
    }
```

`ITrackModel.kt`
```
/**
 * 定义数据填充能力
 */
interface ITrackModel : Serializable {
    /**
     * 数据填充
     */
    fun fillTrackParams(params: TrackParams)
}
```

#### 5.3 触发事件埋点

在需要埋点的地方，直接通过定义在 View 上的扩展函数 `trackEvent(事件名)`触发埋点事件，它会以该扩展函数的接收者对象为起点，逐级向上层视图节点收集参数。另外，它还有多个定义在 Activity、Fragment、ViewHolder 上的扩展函数，但最终都会调用到 View.trackEvent。

```
class MyGoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     fun bind(item: GoodsItem) {
        ...
        trackEvent(GOODS_EXPOSE)
    }
}
```

`EasyTrackUtils.kt`
```
@JvmOverloads
fun Activity?.trackEvent(eventName: String, params: TrackParams? = null) =
    findRootView(this)?.doTrackEvent(eventName, params)

@JvmOverloads
fun Fragment?.trackEvent(eventName: String, params: TrackParams? = null) =
    this?.requireView()?.doTrackEvent(eventName, params)

@JvmOverloads
fun RecyclerView.ViewHolder?.trackEvent(eventName: String, params: TrackParams? = null) {
    this?.itemView?.let {
        if (null == it.parent) {
            it.post { it.doTrackEvent(eventName, params) }
        } else {
            it.doTrackEvent(eventName, params)
        }
    }
}

@JvmOverloads
fun View?.trackEvent(eventName: String, params: TrackParams? = null): TrackParams? =
    this?.doTrackEvent(eventName, params)
```

查看 logcat 日志，可以看到以下日志，显示埋点并没有生效。这是因为没有为 EasyTrack 配置埋点数据上报和统计分析的能力。

`logcat 日志`
```
EasyTrackLib: Try track event goods_expose, but the providers is Empty.
```

#### 5.4 实现 ITrackProvider 接口

EasyTrack 的职责在于收集分散的埋点数据，本身没有提供埋点数据上报和统计分析的能力。因此，你需要实现 ITrackProvider 接口进行依赖注入。例如，这里模拟实现友盟数据埋点提供器，在 onInit() 方法中进行初始化，在 onEvent() 方法中调用友盟 SDK 事件上报方法。

`MockUmengProvider.kt`
```
/**
 * 模拟友盟数据上报
 */
class MockUmengProvider : ITrackProvider() {

    companion object {
        const val TAG = "Umeng"
    }

    /**
     * 是否启用
     */
    override var enabled = true

    /**
     * 名称
     */
    override var name = TAG

    /**
     * 初始化
     */
    override fun onInit() {
        Log.d(TAG, "Init Umeng provider.")
    }

    /**
     * 执行事件上报
     */
    override fun onEvent(eventName: String, params: TrackParams) {
        Log.d(TAG, params.toString())
    }
}
```

#### 5.5 配置 EasyTrack

在应用初始化时，进行 EasyTrack 的初始化配置。我们可以将相关的初始化代码单独封装起来，例如：

`StatisticsUtils.kt`
```
// 模拟友盟数据统计提供器
val umengProvider by lazy {
    MockUmengProvider()
}

// 模拟神策数据统计提供器
val sensorProvider by lazy {
    MockSensorProvider()
}

/**
 * 初始化 EasyTrack，在 Application 初始化时调用
 */
fun init(context: Context) {
    configStatistics(context)
    registerProviders(context)
}

/**
 * 配置
 */
private fun configStatistics(context: Context) {
    // 调试开关
    EasyTrack.debug = BuildConfig.DEBUG
    // 页面间参数映射
    EasyTrack.referrerKeyMap = mapOf(
        CUR_PAGE to FROM_PAGE,
        CUR_TAB to FROM_TAB
    )
}

/**
 * 注册提供器
 */
private fun registerProviders(context: Context) {
    EasyTrack.registerProvider(umengProvider)
    EasyTrack.registerProvider(sensorProvider)
}
```
`EventConstants.java`
```
public static final String FROM_PAGE = "from_page";
public static final String CUR_PAGE = "cur_page";
public static final String FROM_TAB = "from_tab";
public static final String CUR_TAB = "cur_tab";
```

|配置|类型|描述|
|:---|:---|:---|
|debug|Boolean|调试开关|
|referrerKeyMap|Map<String,String>|全局页面间参数映射|
|registerProvider()|ITrackProvider|底层数据埋点能力|

以上步骤是 EasyTrack 的必选步骤，完成后重新执行 trackEvent() 后可以看到以下日志：

`logcat 日志`
```
/EasyTrackLib:  
onEvent：goods_expose
goods_id= 10000
goods_name = 商品名
Try track event goods_expose with provider Umeng.
Try track event goods_expose with provider Sensor.
------------------------------------------------------
```

#### 5.6 页面间参数映射

上一节中有一个`referrerKeyMap`配置项，**定义了全局的页面间参数映射。** 举个例子，在分析不同入口的转化率时，不仅仅需要上报当前页面的数据，还需要上报来源页面的信息。这样我们才能分析用户经过怎样的路径来到当前页面，并最终触发了某个行为。

需要注意的是，来源页面的参数往往不能直接添加到当前页面的埋点参数中，这里一般会有一定的转换规则 / 映射关系。例如：**来源页面的 cur_page 参数，在当前页面应该映射为 from_page 参数。** 在这个例子里，我们配置的映射关系是：

- 来源页面的 cur_page 映射为当前页面的 from_page；
- 来源页面的 cur_tab 映射为当前页面的 from_tab。

因此，假设来源页面传递给当前页面的参数是 A，则当前页面在触发事件时的收集参数是 B：

```
A (来源页面)：
{
    "cur_page" : "list"
    ...
}

B (当前页面)：
{
    "cur_page" : "detail",
    "from_page" : "list",
    ...
}
```

`BaseTrackActivity` 实现了页面间参数映射，你可以创建 BaseActivity 类并继承于 BaseTrackActivity，或者将其内部的逻辑迁移到你的 BaseActivity 中。这一步是可选的，如果你不使用页面间参数映射的特性，你那大可不必使用 BaseTrackActivity。

|操作|描述|
|:---|:---|
|定义映射关系|1、EasyTrack.referrerKeyMap 配置项<br>2、重写 BaseTrackActivity #referrerKeyMap() 方法|
|传递页面间参数|Intent.referrerSnapshot(TrackParams) 扩展函数|

`MyGoodsDetailActivity.java`
```
public class MyGoodsDetailActivity extends MyBaseActivity {

    private static final String EXTRA_GOODS = "extra_goods";

    public static void start(Context context, GoodsItem item, TrackParams params) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(EXTRA_GOODS, item);
        EasyTrackUtilsKt.setReferrerSnapshot(intent, params);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    protected String getCurPage() {
        return GOODS_DETAIL_NAME;
    }

    @Nullable
    @Override
    public Map<String, String> referrerKeyMap() {
        Map<String, String> map = new HashMap<>();
        map.put(STORE_ID, STORE_ID);
        map.put(STORE_NAME, STORE_NAME);
        return map;
    }
}
```

需要注意的是，BaseTrackActivity 不会将来源页面的全部参数都添加到当前页面的参数中，**只有在全局 referrerKeyMap 配置项或 referrerKeyMap() 方法中定义了映射关系的参数，才会添加到当前页面。** 例如：MyGoodsDetailActivity 继承于 BaseActivity，并重写 referrerKeyMap() 定义了感兴趣的参数（STORE_ID、STORE_NAME）。最终触发埋点时的日志如下：

`logcat 日志`
```
/EasyTrackLib:  
onEvent：goods_detail_expose
goods_id= 10000
goods_name = 商品名
store_id = 10000
store_name = 商店名
from_page = Recommend
cur_page = goods_detail
Try track event goods_expose with provider Umeng.
Try track event goods_expose with provider Sensor.
------------------------------------------------------
```

在一般的埋点模型中，每个 Activity (页面) 都有对应一个唯一的 page_id，因此你可以重写 fillTrackParams()  方法追加这些固定的参数。例如：MyBaseActivity 定义了  getCurPage() 方法，子类可以通过重写  getCurPage() 来设置 page_id。

`MyBaseActivity.java`
```
abstract class MyBaseActivity : BaseTrackActivity() {

    @CallSuper
    override fun fillTrackParams(params: TrackParams) {
        super.fillTrackParams(params)
        // 填充页面统一参数
        getCurPage()?.also {
            params.setIfNull(CUR_PAGE, it)
        }
    }

    protected open fun getCurPage(): String? = null
}
```

#### 5.7 TrackParams 参数容器

TrackParams 是 EasyTrack 收集参数的中间容器，最终会分发给 ITrackProvider 使用。

|方法|描述|
|:---|:---|
|set(key: String, value: Any?)|设置参数，无论无何都覆盖|
|setIfNull(key: String, value: Any?)|设置参数，如果已经存在该参数则丢弃|
|get(key: String): String?|获取参数值，参数不存在则返回 null|
|get(key: String, default: String?)|获取参数值，参数不存在则返回默认值 default|

#### 5.8 使用 Kotlin 委托依附参数

如果你觉得每次定义 ITrackModel 数据节点后都需要调用 View.trackModel，你可以使用我定义的 Kotlin 委托 “跳过” 这个步骤，例如：

`MyFragment.kt`
```
private val trackNode by track()
```

`EasyTrackUtils.kt`
```
fun <F : Fragment> F.track(): TrackNodeProperty<F> = FragmentTrackNodeProperty()

fun RecyclerView.ViewHolder.track(): TrackNodeProperty<RecyclerView.ViewHolder> =
    LazyTrackNodeProperty() viewFactory@{
        return@viewFactory itemView
    }

fun View.track(): TrackNodeProperty<View> = LazyTrackNodeProperty() viewFactory@{
    return@viewFactory it
}
```

如果你还不了解委托属性，可以看下我之前写过的一篇文章，这里不解释其原理了：[Android | ViewBinding 与 Kotlin 委托双剑合璧](https://juejin.cn/post/6958346113552220173)

---
# 6. EasyTrack 核心源码

这一节，我简单介绍下 EasyTrack 的核心源码，最核心的部分在入口类 EasyTrack 中：

#### 6.1 doTrackEvent()

doTrackEvent() 是触发埋点的主方法，主要流程是调用 fillTrackParams() 收集埋点参数，再将参数分发给有效的 ITrackProvider。

```
internal fun Any.doTrackEvent(eventName: String, otherParams: TrackParams? = null): TrackParams? {
    1. 检查是否有有效的 ITrackProvider
    2. 基于视图树递归收集埋点参数（fillTrackParams）
    3. 日志
    4. 将收集到的埋点参数分发给有效的 ITrackProvider
}
```

#### 6.2 fillTrackParams()

```
-> 基于视图树递归收集埋点参数
internal fun fillTrackParams(node: Any?, params: TrackParams? = null): TrackParams {
    val result = params ?: TrackParams()
    var curNode = node
    while (null != curNode) {
        when (curNode) {
            is View -> {
                // 1. 视图节点
                if (android.R.id.content == curNode.id) {
                    // 1.1 Activity 节点
                    val activity = getActivityFromView(curNode)
                    if (activity is IPageTrackNode) {
                        // 1.1.1 IPageTrackNode节点（处理页面间参数映射）
                        activity.fillTrackParams(result)
                        curNode = activity.referrerSnapshot()
                    } else {
                        // 1.1.2 终止
                        curNode = null
                    }
                } else {
                    // 1.2 Activity 视图子节点
                    curNode.trackModel?.fillTrackParams(result)
                    curNode = curNode.parent
                }
            }
            is ITrackNode -> {
                // 2. 非视图节点
                curNode.fillTrackParams(result)
                curNode = curNode.parent
            }
            else -> {
                // 3. 终止
                curNode = null
            }
        }
    }
    return result
}
```

主要逻辑：从入参 node 为起点，循环获取依附在视图节点上的 ITrackModel 数据节点并调用 fillTrackParams() 方法收集参数，并将循环指针指向 parent。

---
# 7. 总结

EasyTrack 框架的源码我已经放在 Github 上了，源码地址：https://github.com/pengxurui/EasyTrack 我也写了一个简单的 Sample Demo，你可以直接运行体验下。欢迎批评，欢迎 Issue~

说说目前遇到的问题，在处理页面间参数传递时，我们需要依赖 Intent extras 参数。这就导致我们需要在大量创建 Intent 的地方都加入来源页面的埋点参数（注意：即使你不使用 EasyTrack，你也要这么做）。目前我还没有想到比较好的方法，你觉得呢？说说你的看法吧。

---
#### 参考资料
- [西瓜客户端埋点实践：基于责任链的埋点框架](https://mp.weixin.qq.com/s/iMn--4FNugtH26G90N1MaQ) —— 何金海（字节）著
- [埋点治理：如何把 App 埋点做到极致？](https://mp.weixin.qq.com/s/O_02RsP9U2N4cXQH5rc0zQ) —— 林乐洋（58）著
- [51 信用卡 Android 自动埋点实践](https://mp.weixin.qq.com/s/P95ATtgT2pgx4bSLCAzi3Q) —— 李传志（51 信用卡）著
- [利用 Live Templates 打造埋点自动化利器](https://juejin.cn/post/6966758717773578270) —— 字节大力智能技术团队 著
- [数据分析从理念到实操](https://sensorsdata.cn/uploads/773b55bdee9e1884963493cda4e73aa1/%E6%95%B0%E6%8D%AE%E5%88%86%E6%9E%90%E4%BB%8E%E7%90%86%E5%BF%B5%E5%88%B0%E5%AE%9E%E6%93%8D%E7%99%BD%E7%9A%AE%E4%B9%A6.pdf) —— 神策数据 著

> **创作不易，你的「三连」是丑丑最大的动力，我们下次见！**

## Donate

如果本仓库对你有帮助，可以请小彭喝杯速溶咖啡。

![](https://github.com/pengxurui/AndroidFamily/blob/master/images/%E8%AF%B7%E5%B0%8F%E5%BD%AD%E5%96%9D%E6%9D%AF%E9%80%9F%E6%BA%B6%E5%92%96%E5%95%A1.png)

## License

Copyright [2022] [Peng Xurui]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

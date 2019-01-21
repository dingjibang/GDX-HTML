# GDX-HTML


用HTML+CSS+JS构建libGDX的UI!

![Image](https://raw.githubusercontent.com/dingjibang/GDX-HTML/master/readme/show2.png)
![Image](https://raw.githubusercontent.com/dingjibang/GDX-HTML/master/readme/show1.png)


### 目前项目仍在开发

<br>

# 怎么用

    Stage stage = HTMLStage.buildPath(path-to-html-file);
    //done!

# 支持的CSS属性和HTML

#### ":green_heart:" 标志代表完全支持，而且不损耗性能，建议使用。
###### (这些属性一般LibGDX里都有，所以基本不损耗性能)
#### ":heart:" 标志代表完全支持，但是稍微损耗性能。
###### (这些属性的实现都是模拟/兼容出来的，可能稍微损耗点性能，但是这是个构建引擎（也就是将html+css转译为scene2d），渲染还是LibGDX的scene2d渲染，所以所谓的“损耗”也只是在创建stage时多卡顿了0.001秒而已，而真正渲染起来还是丝滑满帧。)
#### ":blue_heart:" 标志着正在实现。
###### (我正在实现它们呢。)
#### ":broken_heart:" 标志着不支持的属性 / 和浏览器版本的HTML+CSS不同的地方。
###### (不支持代表他们太浪费性能，或者仅仅是我太菜实现不出来hhhh)
<br>
<br>
<br>

## 盒模型
- :green_heart: **width** (px / em)
- :green_heart: **height** (px / em)
- :green_heart: **padding** (padding-left / right / top / bottom)
- :green_heart: **margin** (margin-left(auto) / right(auto) / top / bottom)

## 文本控制
- :green_heart: **font-size** (px)
- :green_heart: **color** (color-name / hex / rgb / rgba)
- :green_heart: **text-align** (left / center / right)
- :heart: **line-height** (px)
   - ###### 设置“line-height”属性，可能会让界面layout多次。
   - ###### 使用“line-height”可能会损耗性能（仅构建过程中）。
   - ###### 如果只有单行文本，推荐使用padding或者设置高度的方法，尽量别用line-height。  
- :heart: **letter-spacing** (px)
	- ###### 设置“letter-spacing”属性，可能会让界面layout多次。
    - ###### 使用“letter-spacing”可能会损耗性能（仅构建过程中）。
- :green_heart: **-gdx-markup** (true)
	- ###### 开启后，将会支持LibGDX的markup语法，就是用来使用多彩文本的那个东西。
    - ###### 当开启后，就无法关闭了，哪怕你设置为false。
    - ###### 相比使用一个个div/span，在设置他们的color属性，-gdx-markup会更加轻量，不过用起来费劲就是了。
- :heart: **-gdx-wrap** (true / false)
	- ###### 当设置-gdx-wrap为true时，文本将会启用自动换行，但是换行需要给这个文本（或者父元素）一个确定的宽度。
    - ###### -gdx-wrap默认是关闭的(false)，开启后可能会让界面layout多次，损耗性能（仅构建过程中）。 
    - ###### :broken_heart: 当你开启了-gdx-wrap后，不 允 许 当前元素再包含任何子元素，这是一个无法实现的问题。如果你想让当前文本颜色是多彩的，可以使用-gdx-markup属性（见上方）

## 图片 / 纹理
- :green_heart: **&lt;img async="true" src="..." /&gt;**
- :green_heart: **-gdx-image-scaling**
    - ###### 图片（&lt;img /&gt;）是异步加载的，这样就不会阻塞构建引擎了，但是图片加载完毕之后，会重新layout整个界面，这可能会造成界面“闪”一下，你可以设置&lt;img async="false" src="..." /&gt;，这样就会使用同步加载。也可以给img提前设置一个固定的宽高。
	- ###### 如果你想给&lt;img /&gt;设置Scaling，你可以使用-gdx-image-scaling属性，它的值对应了com.badlogic.gdx.utils.Scaling这个枚举类, 比如 "fit" 或者 "none".

## 丰富的背景
- :green_heart: **background-color** (color-name / hex / rgb / rgba)
- :blue_heart: **background-image**
- :blue_heart: **background-position**
- :blue_heart: **background-size**

## 元素定位方法（Position）
- :green_heart: **position: static**
- :blue_heart: **position: relative**
- :blue_heart: **position: absolute**
- :blue_heart: **position: fixed**
- :broken_heart: **position: sticky**

## 元素实现方法（Display）
- :green_heart: **display: inline**
- :heart: **display: inline-block**
- :heart: **display: block**
	- ###### display: block / inline-block的实现方法很蠢，所以在某些情况下，显示的和浏览器不一样。
- :blue_heart: **display: table**

## 表格布局（Table）
- :green_heart: **&lt;table /&gt;** with **display: table**
- :green_heart: **&lt;tr /&gt;** with **display: table-row**
- :green_heart: **&lt;td /&gt; &lt;th /&gt;** with **display: table-cell**
- :green_heart: **vertical-align**
    - ###### :broken_heart: 在Table里直接放一个div，永远都是非法的，除非你给这个div设置css属性为display: table-cell。否则因此产生的布局问题概不负责。
    - ###### :broken_heart: 为了性能，Table不支持margin属性，非要使用的话，可以在外面包围个div，然后给这个div设置padding属性。
    - ###### :broken_heart: Table的显示有时候过于玄学，我已经放弃了和浏览器一致性了，所以有时候显示的和浏览器不一样我也没办法了hhhhh。
    - ###### :broken_heart: TBody, THead和TR都是假的，他们仅仅用来给表格换一行，不要在上面用css属性。
    - ###### 每个单元格（Cell）默认都会延展（expand），也就是宽高不是固定的，是自动计算的，如果这个单元格的宽高和你预想的不一样，你可以给它设置一个固定的宽高。 

## 字体
- :blue_heart: **font-family**

## 边框
- :blue_heart: **border**

## 漂浮（Float）
- :broken_heart: **不可能实现的，float是最傻〇的css属性。没有float也能实现所有的东西。**

## 选择器(Selector) / 伪选择器
- :blue_heart: **:hover**
- :blue_heart: **:active**
- :green_heart: **:lt / :gt / :eq / :first-child / :last-child**
- :green_heart: **:has(selector) / :not(selector)**
- :green_heart: **:contains(text)**
- :green_heart: **[点我查看](https://jsoup.org/apidocs/org/jsoup/select/Selector.html) 所有支持的选择器 / 伪选择器**

## 事件监听（Event listener）
- :blue_heart: Coming soon.

## JS脚本（Javascript）
- :blue_heart: Coming soon.


## 更多...
- :green_heart: :green_heart: :green_heart:

# 代码滞销，帮帮我
- 这项目的饼有点大，可能消化不了，希望有大牛可以帮帮我一起实现_(:3」∠)_
	- 邮箱: dingjibang@qq.com
	- QQ: 1406547525

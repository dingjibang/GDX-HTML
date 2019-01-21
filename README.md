# GDX-HTML

Using HTML + CSS + JS to build libGDX UI!

用HTML+CSS+JS构建libGDX的UI!

#### [点我查看中文说明](https://github.com/dingjibang/GDX-HTML/blob/master/README_CN.md)

![Image](https://raw.githubusercontent.com/dingjibang/GDX-HTML/master/readme/show2.png)
![Image](https://raw.githubusercontent.com/dingjibang/GDX-HTML/master/readme/show1.png)


### 仍在开发(Still developing)

<br>

# 怎么用(how to use)

    Stage stage = HTMLStage.buildPath(path-to-html-file);
    //done!

# support CSS / HTML

#### ":green_heart:" is full support, and light performance cost css styles
###### (Close to LibGDX properties, so don't need to spend too much performance)
#### ":heart:" is full support, and is heavy performance cost css styles
###### (In order to be compatible with css properties, some heavy-performance-cost hack tricks have been used)
#### ":blue_heart:" is coming soon
###### (It's in dev)
#### ":broken_heart:" is unsupport css styles / different with real world html+css
###### (It may be too wasteful performance, or just too difficult to implementation :sob:)
<br>
<br>
<br>

## Box
- :green_heart: **width** (px / em)
- :green_heart: **height** (px / em)
- :green_heart: **padding** (padding-left / right / top / bottom)
- :green_heart: **margin** (margin-left(auto) / right(auto) / top / bottom)

## Text control
- :green_heart: **font-size** (px)
- :green_heart: **color** (color-name / hex / rgb / rgba)
- :green_heart: **text-align** (left / center / right)
- :heart: **line-height** (px)
   - ###### Setting the line-height property will cause the text dom to be layout multiple times.
   - ###### The scene2d.Label of libgdx does not support line-height. For compatibility, it will cost additional performance
   - ###### So if there is only a single line of text, it is recommended to set padding / height.  
- :heart: **letter-spacing** (px)
	- ###### Setting the letter-spacing property will cause the text dom to be layout multiple times.
	- ###### The scene2d.Label of libgdx does not support letter-spacing. For compatibility, it will cost additional performance
- :green_heart: **-gdx-markup** (true)
	- ###### LibGDX text markup color language support.
    - ###### Once set, it will not be canceled, even if you set it to "false".
- :heart: **-gdx-wrap** (true / false)
	- ###### The default is FALSE to save performance. When enabled, LibGDX native wrap support will be used, but the element or parent(s) element must be set to a fixed width.
    - ###### Setting the gdx-wrap property will cause the font size to be layout multiple times. 
    - ###### :broken_heart: **When you set an element to "-gdx-wrap: true", it is not allowed to contain any child elements (except plain text), If you want to use colorful text, please set -gdx-markup to true.**

## Image / Texture
- :green_heart: **&lt;img src="..." /&gt;**
- :green_heart: **-gdx-image-scaling**
    - ###### Image is loaded asynchronously by default, which does not block the rendering of html, but when the Image is loaded, it will be re-layout, which will make the whole interface flash and affect performance. If your image is not very large, ***set &lt;img async="false" src="..." /&gt;*** and then becomes synchronous load. You can also preset a fixed width and height for the Image.
	- ###### To set the scaling of &lt;img /&gt;, you can use this css property, value is enum name of com.badlogic.gdx.utils.Scaling, like "fit" or "none".

## Rich background
- :green_heart: **background-color** (color-name / hex / rgb / rgba)
- :blue_heart: **background-image**
- :blue_heart: **background-position**
- :blue_heart: **background-size**

## Positioning method
- :green_heart: **position: static**
- :blue_heart: **position: relative**
- :blue_heart: **position: absolute**
- :blue_heart: **position: fixed**
- :broken_heart: **position: sticky**

## Display
- :green_heart: **display: inline**
- :heart: **display: inline-block**
- :heart: **display: block**
	- ###### display block / inline-block uses an ugly implementation, sometimes it may not be the same as what the browser shows.
- :blue_heart: **display: table**

## Table layout
- :green_heart: **&lt;table /&gt;** with **display: table**
- :green_heart: **&lt;tr /&gt;** with **display: table-row**
- :green_heart: **&lt;td /&gt; &lt;th /&gt;** with **display: table-cell**
- :green_heart: **vertical-align**
    - ###### :broken_heart: *Table* directly contains a *Div* is illegal, unless the div is set to display: table-cell, otherwise any display is weird.
    - ###### :broken_heart: For performance, &lt;table&gt; or display: table, is not support margin or padding properties. As an alternative, you can include a container outside of &lt;table&gt; and set padding.
    - ###### :broken_heart: Table is too metaphysical, it is likely to be different from the browser display.
    - ###### :broken_heart: TBody, THead and TR are fake and will not be read by the rendering engine(Just used to call row()), so don't think of it here. 
    - ###### Each cell will be expand() by default, unless you set a size, if you set the width or height but do not show what you want, you can try to set the size (fixed value, or percentage) for each column of the cell.

## Font
- :blue_heart: **font-family**

## Border
- :blue_heart: **border**

## Float
- :broken_heart: **Nope, float is SHIT.**

## Selectors / Pseudo selectors
- :blue_heart: **:hover**
- :blue_heart: **:active**
- :green_heart: **:lt / :gt / :eq / :first-child / :last-child**
- :green_heart: **:has(selector) / :not(selector)**
- :green_heart: **:contains(text)**
- :green_heart: **[See link](https://jsoup.org/apidocs/org/jsoup/select/Selector.html) to visit all support selectors / pseudo selectors**

## Event listener
- :blue_heart: Coming soon.

## JavaScript Support
- :blue_heart: Coming soon.


## And more...
- :green_heart: :green_heart: :green_heart:

# Help us
- This project is expected to be too large, I need contributors. Please help us.
	- mailto: dingjibang@qq.com
	- QQ: 1406547525

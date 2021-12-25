# ITnews
## 登录，注册，修改（忘记）密码
### login
- 1.登录界面中有注册账号和忘记密码。
- 2.正确输入登录信息后，点击登录按钮，会将token保存至本地，在token过期之前，可以免登录直接进入页面。
### 注册账号
- 1.正确输入邮箱后，点击获取邮箱验证码，就可以获取到注册用的验证码。
### 修改密码
- 1.输入邮箱后，点击获取验证码，就可以获取到修改密码用的验证码，输入新的密码，点击提交便可以成功修改密码。
## 主界面
### 底部导航栏
- 1.底部有三个导航栏，点击新闻，会转到新闻的列表；点击我的新闻，会转到发布新闻的界面；点击个人信息，可以查看个人信息。
### 新闻
- 1.上拉刷新，下拉加载：向上拉会加载出最新内容，向下划会加载过往内容
- 2.新闻以列表形式展现，设置了分割线。每一条新闻以新闻标题和新闻封面图片构成。
    - 点击新闻：跳转到新闻的具体页面，顶格显示文章标题，往下是文章的作者及其头像。再往下是文章内容，由图片和文字组成。
    - 新闻具体内容底部有点赞和收藏的图标，可以正常进行点赞和收藏。
### 发布新闻
- 1.点击发布新闻的按钮会跳转到发布新闻的界面：
    - 最上方填写文章的标题
    - 往下填写文章的内容
    - 加号按钮可以添加图片
    - 点击发布新闻就可以成功发布新闻
### 个人信息
- 1.个人信息包括头像，昵称，个人简介，收藏文章数，性别。下方有重新登录和修改密码的按钮。
- 2.点击个人信息会跳转到修改个人信息的界面
    - 输入新的昵称
    - 输入新的简介
    - 点击性别会弹出对话框，选择男女
    - 点击确认，便可以修改成功
- 3.点击头像会弹出对话框，选择相册内的照片或是重新拍照。
## 关于细节
- 1.本程序限制了禁止横屏
- 2.断网有提示
- 3.任何输入都不能为空
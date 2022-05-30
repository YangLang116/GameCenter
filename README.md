<!-- Plugin description -->
GameCenter is an IDEA Plugin for Developer to play Games
<!-- Plugin description end -->

## 项目说明

为了缓解日复一日枯燥编程带来了的压力，由此`Game Center`插件诞生。`Game Center`顾名思义，是在`IDEA`中集成一个游戏菜单，方便开发同学工作之余使用。

## 使用截图
![plugin display](https://iflutter.toolu.cn/configs/game_display.png)

## 游戏列表
- FlappyBird - 从项目 [FlappyBird](https://github.com/kingyuluk/FlappyBird) 进行移植

## 项目未来规划
- 移植FC游戏

---

## 插件扩展
考虑该插件的扩展性，降低后期的开发成本，方便更多的游戏集成。`Game Center` 支持将每一个游戏以 `jar` 文件的形式进行引入。


### 开发环境要求
- java 11

### 开发步骤

- 1、编写Swing游戏
- 2、引用 `GameCenterBase.jar`依赖(可选)

```
GameCenterBase 依赖包，提供音频播放、数据存储功能：

数据存储：

GameCenterFacade<? extends GameCenterService> gameCenterFacade = GameCenterFacade.getInstance();
StorageService storageService = gameCenterFacade.getStorageService();
storageService.save(key,value) / storageService.read(key)

音频播放：

GameCenterFacade<?> gameCenterFacade = GameCenterFacade.getInstance();
AudioService audioService = gameCenterFacade.getAudioService();
InputStream audioStream = getResourceAsStream(assetPath);
audioService.play(audioStream);
```

- 3、为Swing游戏添加指定入口函数，以便 `Game Center` 能运行该游戏
    ```
    public static void runGame() {
      ...
    }
    ```

- 4、将编写的Swing游戏导出成jar文件

- 5、注入游戏到 `Game Center` IDEA插件
    - 将第4步生成的jar，放入项目根目录的`libs`文件夹
    - 在`src/main/resources/conf/conf.properties` 中配置游戏，格式如下：

  ```
  Game Name=runGame 函数所在的类的FQN，例如：FlyBird=com.kingyu.flappybird.app.App
  ```

- 6、将Swing游戏的源代码，放入到 `game-list` 中(可选)

- 7、提交PR，游戏运行无误，即可发布上市场

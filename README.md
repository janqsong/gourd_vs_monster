* [葫芦娃大战妖精](#葫芦娃大战妖精)
  * [1 功能介绍](#1-功能介绍)
    * [1\.1 联机对战](#11-联机对战)
      * [1\.1\.1 联机逻辑介绍](#111-联机逻辑介绍)
      * [1\.1\.2 游戏逻辑介绍](#112-游戏逻辑介绍)
      * [1\.1\.3 网络协议设计](#113-网络协议设计)
    * [1\.2 本地回放](#12-本地回放)
    * [1\.3 关于游戏](#13-关于游戏)
  * [2 代码分析](#2-代码分析)
    * [2\.1 包com\.ttf\.gourd\.stage](#21-包comttfgourdstage)
    * [2\.2 包com\.ttf\.gourd\.server](#22-包comttfgourdserver)
    * [2\.3 包com\.ttf\.gourd\.client](#23-包comttfgourdclient)
    * [2\.4 包com\.ttf\.gourd\.protocol](#24-包comttfgourdprotocol)
    * [2\.5 包com\.ttf\.gourd\.ai](#25-包comttfgourdai)
      * [2\.5\.1 内容](#251-内容)
      * [2\.5\.2 接口介绍](#252-接口介绍)
      * [2\.5\.3 ai开发过程](#253-ai开发过程)
      * [2\.5\.4 可扩展性](#254-可扩展性)
    * [2\.6 包 com\.ttf\.gourd\.bullet](#26-包-comttfgourdbullet)
      * [2\.6\.1 内容](#261-内容)
      * [2\.6\.2 Bullet子弹类的设计](#262-bullet子弹类的设计)
      * [2\.6\.3 子弹的各种状态](#263-子弹的各种状态)
    * [2\.7 包com\.ttf\.gourd\.collision](#27-包comttfgourdcollision)
      * [2\.7\.1 内容](#271-内容)
    * [2\.8 包com\.ttf\.gourd\.Creature](#28-包comttfgourdcreature)
      * [2\.8\.1 内容](#281-内容)
      * [2\.8\.2 包内类的关系](#282-包内类的关系)
      * [2\.8\.3 Creature类的设计](#283-creature类的设计)
      * [2\.8\.4 Creature子类的设计](#284-creature子类的设计)
      * [2\.8\.5 CreatureFactory类的设计](#285-creaturefactory类的设计)
      * [2\.8\.6 CreatureStateWithClockand CreatureState](#286-creaturestatewithclockand-creaturestate)
      * [2\.8\.7 ImagePosition类的设计](#287-imageposition类的设计)
    * [2\.9 包com\.ttf\.gourd\.Equipment](#29-包comttfgourdequipment)
      * [2\.9\.1 内容](#291-内容)
      * [2\.9\.2 各个类的关系](#292-各个类的关系)
      * [2\.9\.3 Equipment抽象类的设计](#293-equipment抽象类的设计)
      * [2\.9\.4 Equipment子类设计](#294-equipment子类设计)
      * [2\.9\.5 EquipmentFactory装备工厂设计](#295-equipmentfactory装备工厂设计)
    * [2\.10 包com\.ttf\.gourd\.localplayback](#210-包comttfgourdlocalplayback)
    * [2\.11 包com\.ttf\.gourd\.constant](#211-包comttfgourdconstant)
      * [2\.11\.1 内容](#2111-内容)
    * [2\.12 包com\.ttf\.gourd\.tool](#212-包comttfgourdtool)
  * [3 游戏测试](#3-游戏测试)
    * [3\.1 单元测试](#31-单元测试)
      * [3\.1\.1 测试通信中的输入输出的协议是否能够正确解析](#311-测试通信中的输入输出的协议是否能够正确解析)
    * [3\.2 胜率测试](#32-胜率测试)
      * [3\.2\.1 预期目标](#321-预期目标)
      * [3\.2\.2 游戏测试数据](#322-游戏测试数据)
      * [3\.2\.3 游戏测试文件](#323-游戏测试文件)
  * [4 开发过程](#4-开发过程)
    * [4\.1 需求分析与策划](#41-需求分析与策划)
    * [4\.2 模块划分与任务分配](#42-模块划分与任务分配)
    * [4\.3 单机版实现](#43-单机版实现)
    * [4\.4 联机同步实现](#44-联机同步实现)
    * [4\.5 功能和文档完善](#45-功能和文档完善)
  * [5 写给玩家](#5-写给玩家)
    * [5\.1 玩法说明](#51-玩法说明)
    * [5\.2 葫芦娃阵营详细数据](#52-葫芦娃阵营详细数据)
    * [5\.3 妖精阵营详细数据](#53-妖精阵营详细数据)
    * [5\.4 装备详细数据](#54-装备详细数据)

# 葫芦娃大战妖精

[开发项目地址](https://github.com/JansonSong/gourd_vs_monster)

## 1 功能介绍

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151439248.png)

<img src="https://img-blog.csdnimg.cn/20210101151439248.png" alt="startScene" style="zoom:40%;" />

### 1.1 联机对战

#### 1.1.1 联机逻辑介绍

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151432665.png)

<img src="https://img-blog.csdnimg.cn/20210101151432665.png" alt="connectScene" style="zoom:40%;" />

创建服务器：输入`Server Port`，再点击“创建服务器”来使用此电脑进行创建服务器，会提示创建成功或者失败。

连接服务器：输入正确的`Server IP`和`Server Port`，点击“连接服务器“来连接服务器，会提示连接成功或者失败。

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151431538.png)

<img src="https://img-blog.csdnimg.cn/20210101151431538.png" alt="creatureSucess" style="zoom:75%;" />

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151430417.png)

<img src="https://img-blog.csdnimg.cn/20210101151430417.png" alt="connectSucess" style="zoom:75%;" />

#### 1.1.2 游戏逻辑介绍

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/2021010115143838.png)

<img src="https://img-blog.csdnimg.cn/2021010115143838.png" alt="prepareGame" style="zoom:40%;" />

点击“准备开始”，当两个客户端都点击之后，进入到排兵布阵界面，玩家有30秒时间，通过移动该阵营的生物到界面上，进行排兵布阵。

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151438338.png)

<img src="https://img-blog.csdnimg.cn/20210101151438338.png" alt="prepare" style="zoom:40%;" />

30秒倒计时结束之后，玩家双方的界面会同步，然后进行游戏。

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151438550.png)

<img src="https://img-blog.csdnimg.cn/20210101151438550.png" alt="fightScene" style="zoom:40%;" />

> 具体的游戏逻辑将在“关于游戏”这个功能中介绍。

#### 1.1.3 网络协议设计

服务器和两个玩家建立起连接之后，通过传输字符流来保持界面一致，协议便是保证通信的必要和重要手段。

协议分为需要解析协议和不需要解析协议。

不需要解析协议指，用户获得该协议的类型之后，根据类型进行操作，而不需要其他额外的信息。

需要解析协议指，根据用户传过来的消息类型，进行相应的解析，以获取传过来的信息。

### 1.2 本地回放

在打开游戏的开始界面，有”本地回放“按钮，用户点击”本地回放“按钮之后，会跳转到回放文件目录界面，这个界面首先会加载某个指定文件夹下的文件，如果该文件夹下存在回放文件（这里区分采用文件的后缀.back，来区分该文件是否为本地回放文件），将会将这些文件的相关信息加载到屏幕上，用户可以通过双击，或者选择后，点击右下角的播放进行本地回放。我们本次实验的回放，将会放到`"../playbackFiles"`文件夹（即与`gourd_vs_monster`文件夹同一个目录下）下。

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151435650.png)

<img src="https://img-blog.csdnimg.cn/20210101151435650.png" alt="playbackFile" style="zoom:40%;" />

用户还可以通过导入单个文件和批量导入文件来加载本地回放文件，如果文件名合法并且内容合法，就可以进行回放操作。加载进去之后，需要等待3秒钟，才会开始进行回放。

在回放界面，用户可以通过键盘上的`'1', '2', '3', '4', '5', '6', '7', '8', '9', 'q', 'w', 'e', 'r', 't'`按键（分别对应，“大娃”、”二娃“、”三娃“、”四娃“、”五娃“、”六娃“、”七娃“、”爷爷“、”穿山甲“、”蛇精“、”蝎精“、“蜈蚣精”、“蝙蝠精”、“鳄鱼精”）进行对生物的选择，选择之后会在屏幕的左边显示出该生物的图片以及生物的状态信息（比如，生命值，魔法值，攻击力、振奋（技能效果）等）。

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101151434584.png)

<img src="https://img-blog.csdnimg.cn/20210101151434584.png" alt="playbackImage" style="zoom:40%;" />

回放界面的左边，会有倍速播放回放按钮，用户可以点击来调整回放的播放速度，并且用户可以通过暂停和开始按钮来控制回放的暂停和开始，也可以通过返回按钮，回到选择回放文件界面。

> 
>
> 打开本地回放，显示导入回放文件失败的解决方法。
>
> 

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101213013257.png)

<img src="https://img-blog.csdnimg.cn/20210101213013257.png" alt="playbackImage" style="zoom:40%;" />

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101213020603.png)

点击添加回放文件或者批量导入（回放文件在`playbackFiles`文件夹中，以`.back`结尾的文件为回放文件)

<img src="https://img-blog.csdnimg.cn/20210101213020603.png" alt="playbackImage" style="zoom:40%;" />

[下面的图片如果显示不出来，点这里](https://img-blog.csdnimg.cn/20210101213027391.png)

此时就可以通过双击或者选择后点击回放进行本地回放。

<img src="https://img-blog.csdnimg.cn/20210101213027391.png" alt="playbackImage" style="zoom:40%;" />



### 1.3 关于游戏

作者：Xiang-Xiaoyu 联系方式：xiaoyu-xiang@qq.com

作者：JansonSong 联系方式：1149602149@qq.com

此项目为2020年秋季NJU JAVA大作业，后期可能会进行后续的修改bug以及功能的添加。

游戏中的玩法介绍已经在[5 写给玩家](#5-写给玩家)处给出。



**游戏的运行方式**

在`pom.xml`文件目录下，打开命令终端，输入`mvn package`进行编译，然后输入`java -jar target/gourd_vs_monster-1.0.0.jar`运行游戏，或者直接双击`gourd_vs_monster-1.0.0.jar`包。

## 2 代码分析

### 2.1 包`com.ttf.gourd.stage`

功能：`javafx`显示的界面控制。

代码分析：一些界面采用的`.fxml`文件进行生成，然后用`SceneController.java`对界面进行控制。

- `App.java`用于加载`.fxml`和`.css`文件然后进行窗口显示；
- `SceneController.java`用于控制窗口中控件以及绑定事件等。
- 

### 2.2 包`com.ttf.gourd.server`

功能：用于建立服务器，与客户端通信，保证两个阵营的客户端界面同步，以及保存本地回放文件。

代码分析：

- `GameServer.java`，用于建立服务器，并且等待客户端的连接；
- `SocketController.java`，用于给客户端分配阵营，以及给双方阵营30秒的排兵布阵时间。
- `ServerScene.java`，用于界面的相关信息显示，这里并没有真正显示到屏幕上，而只是将图片以及生物的状态信息进行相应的改变，然后再把这个改变分发给客户端，以达到同步，并且会记录下每次改变到本地文件中，这样可以就行后续的回放。
- `MsgController.java`，用于对通信协议的信息进行解析，以保持服务器和两个客户端的状态信息在一帧之内相同，这个文件与`com.ttf.client`包下的`MsgController.java`以及`com.ttf.localplayback`包下的`ContentParse.java`功能都相同，所以后续在介绍相关的两个包的时候，不会对这个文件进行过多解释。

```java
// GameServer.java

// 建立服务器连接后，等待客户端链接，客户端链接上之后，随机分配阵营
while(true) {
    try {
        if(count == 1) {
            campType = randomNum.nextInt(2);
            Socket socket = serverSocket.accept();
            if(campType == 0)
                // 使用SocketServerController.java分配阵营，即告诉该客户端他是什么阵营的，
                socketServerController.addGourdPlayer(socket);
            else
                socketServerController.addMonsterPlayer(socket);
            count++;
        } else {
            Socket socket = serverSocket.accept();
            if(campType == 0)
                socketServerController.addMonsterPlayer(socket);
            else
                socketServerController.addGourdPlayer(socket);
            break;
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
}
```

```java
// SocketController.java
public void prepareFight() throws IOException {
    // 发送开始准备消息给客户端
    new NoParseMsg(Msg.PREPARE_GAME_MSG).sendMsg(outGourd);
    new NoParseMsg(Msg.PREPARE_GAME_MSG).sendMsg(outMonster);
    // ...等待客户端准备就绪的消息，在新的线程中
    // ...等待客户端准备就绪的消息，在新的线程中

    // 30秒准备倒计时
    for(int i = 0; i < 30; i++) {
        new CountDownMsg(30 - i).sendMsg(outGourd);
        new CountDownMsg(30 - i).sendMsg(outMonster);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 30秒倒计时结束，发送开始游戏的消息给两个客户端
    new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outGourd);
    new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outMonster);
    new ServerScene(serverSocket, socketPlayerGourd, socketPlayerMonster, inGourd, outGourd, inMonster, outMonster).startGame();

}
```

```java
// ServerScene.java 最重要的界面同步

// 初始化资源配置
public void initScene() {
}

// 同步双方摆放阵营的位置信息
public void startGame() throws IOException {
}

// 开始战斗函数
public void startFight() throws IOException {
    // 下面在一个新的线程进行监听葫芦娃阵营客户端传来的信息
    while (true) {
        int gourdMsgType = inGourd.readInt();
        if (gourdMsgType == Msg.FINISH_FLAG_MSG) {
            gourdFinishFlag = true;
        } else if(gourdMsgType == Msg.SOCKET_DISCONNECT_MSG) {
            Thread.sleep(3000);
            inGourd.close();
            outGourd.close();
            gourdSocket.close();
            if(!serverSocket.isClosed())
                serverSocket.close();
            break;
        }
        else {
            gourdMsgController.getMsgClass(gourdMsgType, inGourd);
        }
    }
    // 同理，再开一个线程，监听妖精阵营传来的信息，代码同上，省略
    
	// 在这个while(true)中进行服务器与两个客户端的信息同步，并在这里实时发送信息给客户端
    while (true) {
        // 这里主要是一些信息的设置，比如图片的位置信息设置，生物的状态信息以及子弹和装备的信息设置。
        // 举例说明如何同步：葫芦娃阵营的移动是客户端控制的，客户端通过通信，将位置和状态变动信息传给服务器，服务器在上面监听事件中，进行监听，然后在这个线程中进行获取监听获得的信息，并自己设置位置信息以及将葫芦娃的变动信息发给妖精
    }
}

//根据阵营以及两个family判断是谁获胜了,-1,0,1,2返回值只可能是这四种状态
private int judgeWin(HashMap<Integer, Creature> myFamily, HashMap<Integer, Creature> enemyFamily) {
}

```

```java
// MsgController.java
public class MsgController {
	// 用于解析接收到的消息，根据消息的类型，用对应的协议进行解析，然后获取解析得到的值，为了保证`ServerScene.java`能够获得完整的解析信息，会定义需要临时列表，供`ServerScene.java`调用，然后进行信息同步
    public void getMsgClass(int msgType, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        switch (msgType) {
            case Msg.POSITION_NOTIFY_MSG: {
                break;
            }
            // ......省略了其他消息类型
            default: {
                break;
            }
        }
    }
}
```

### 2.3 包`com.ttf.gourd.client`

功能：用于连接服务器，然后进行不同阵营的初始化以及游戏界面的显示和与服务器进行通信。

代码分析：

- `GameClient.java`用于连接服务器，保持通信
- `Camp.java`用于不同阵营中的相同操作，比如初始化一些都需要初始化的变量，以及摆放生物时的信息同步等
- `GourdCamp.java`和`MonsterCamp.java`用于不同阵营的初始化。
- `GameStartFight.java`用于游戏界面的操作和与服务器相关信息同步的操作。
- `MsgController.java`用于通信的信息解析，与`com.ttf.gourd.server`包中相似，不在赘述。

```java
// GameStartFight.java

public void start() {
    init(campType, myFamily, enemyFamily);
    // 下面是在一个新的线程中操作，用于监听服务器端发来的信息
    while (true) {
        int msgType = in.readInt();
        if (msgType == Msg.FINISH_GAME_FLAG_MSG) {
            msgController.getMsgClass(msgType, in);
            if(campType.equals(msgController.getWinCampType()))
                gameOver(Constant.gameOverState.VICTORY);
            else
                gameOver(Constant.gameOverState.DEFEAT);
            gameOverTimeMillis = System.currentTimeMillis();
            gameOverFlag = true;
        } else if(msgType == Msg.SOCKET_DISCONNECT_MSG) {
            break;
        }
        else
            msgController.getMsgClass(msgType, in);
    }
    
    // 下面是显示游戏界面和发送信息给服务器的while(true)
    while(true) {
    }

}

// 下面是初始化一些绑定事件，用于操纵自己放的生物
private void init(String camp, HashMap<Integer, Creature> myFamily, HashMap<Integer, Creature> enemyFamily) {
}

//游戏结束，播放结束的图片
private void gameOver(int gameOverState) {
}

```

### 2.4 包`com.ttf.gourd.protocol`

功能：这个包主要用于网络通信以及本地回放功能的依赖功能，协议。

代码分析：首先定义一个接口`Msg`，在这个接口中，静态变量定义协议的类型，然后再写多个类用于处理协议内部信息。

```java
// Msg.java
public interface Msg {
    // 负责通知位置变动信息
    public static final int POSITION_NOTIFY_MSG = 4;
    // ...还有很多其他协议类型，详情请参考com.ttf.gourd.protocol下的Msg.java

    // 发送信息函数
    public void sendMsg(ObjectOutputStream outStream) throws IOException;
    // 接受信息函数
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException;
}
```

```java
// 举例：PositionNotifyMsg.java
// 这样定义，用于序列化
class PositionNotify implements Serializable {
    public String campType;
    public int creatureId;
    public double layoutX;
    public double layoutY;
}

// 使用Msg的接口，重写发送和接收函数。
public class PositionNotifyMsg implements Msg {
    private static final int msgType = POSITION_NOTIFY_MSG;
    PositionNotify positionNotify = new PositionNotify();
	
    // 用于接收解析定义用
    public PositionNotifyMsg() {
    }
    
	// 用于发送定义用
    public PositionNotifyMsg(String campType, int creatureId,
                             double layoutX, double layoutY) {
        positionNotify.campType = campType;
        positionNotify.creatureId = creatureId;
        positionNotify.layoutX = layoutX;
        positionNotify.layoutY = layoutY;
    }

    @Override // 重写发送函数
    public void sendMsg(ObjectOutputStream outStream) throws IOException {
        outStream.writeInt(msgType); // 发送协议类型
        outStream.writeObject(positionNotify); // 发送协议中需要传递的内容
        outStream.flush();
    }

    @Override
    public void parseMsg(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        // 在之前会有一个判断这个协议是什么类型的，然后才会调用这个函数，来获取协议中的信息。
        positionNotify = (PositionNotify) inStream.readObject();
    }
	// 下面还有获取这些解析得到的变量信息，没有展现出来
}
```

```java
// 如何使用这些协议
// 发送方
ObjectOutputStream out;
new PositionNotifyMsg("GOURD", 2, 300, 300).sendMsg(out);

// 接收方
ObjectInputStream in;
PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg().parseMsg(in);
String campType = positionNotifyMsg.getCampType();
int creatureId = positionNotifyMsg.getCreatureId();
double layoutX = positionNotifyMsg.getLayoutX();
double layoutY = positionNotifyMsg.getLayoutY();
```



### 2.5 包`com.ttf.gourd.ai`

#### 2.5.1 内容

这个包内主要包括一个接口`AiInterface`,和两个实现该接口的类`FoolAi`和`FirstGenerationAi`，这个接口的设计模式是委托模式,与`Creature`类的关系是聚合，`Creature`将方向的选择与攻击功能交给`AiInterface`来实现

#### 2.5.2 接口介绍

```java
public interface AiInterface {
    //观测
    public Creature observe(Creature myCreature, HashMap<Integer,Creature> enemies);
    //移动方式
    public void moveMod(Creature myCreature, HashMap<Integer,Creature> enemies);
    //攻击模式
    public Bullet aiAttack(Creature myCreature, HashMap<Integer,Creature> enemies);
}
```

该接口定义了三个功能,分别是观测敌军选取攻击目标,基于观测选择移动方式(设置方向),基于观测攻击目标产生子弹

#### 2.5.3 `ai`开发过程

`FoolAi`采用完全随机的选取方向，观测的第一优先级生物是最近的生物,攻击最近的生物。缺点有两点,其一是生物每一帧的方向都可能会发生改变,导致图片频繁抖动。其二是选取最近的生物攻击会导致总体上生物会越来越趋向合在一起,不美观，操作难。

`FirstGenerationAi`综合距离，血量，我方攻击力，敌方防御力等多项因素设置第一优先攻击目标,根据攻击距离以及与攻击目标之间的关系设置方向,并在每次设置方向后锁定方向一段时间,解决了图片抖动问题,使得生物在自动攻击与移动时相对分散且较为智能

#### 2.5.4 可扩展性

这个`ai`接口的可扩展性好,只要实现三个方法就行,可继承`FirstGenerationAi`类实现第二代`ai`，也可通过实现接口的方式为任何一个生物设计`ai`战斗方式



### 2.6 包 `com.ttf.gourd.bullet`

#### 2.6.1 内容

这个包里面包含了子弹类`Bullet`和子弹状态`BulletState`

#### 2.6.2 `Bullet`子弹类的设计

子弹分为近战子弹和远程子弹

近战子弹为各种爪痕，远程子弹为各种不同颜色不同半径的圆球

对于远程子弹而言，它是追踪的，当目标生物死亡或者发生碰撞时，它会消失

对于近战子弹而言，它是打出即碰撞的，没有运行轨迹，是打出必定命中的

任何子弹在且仅在命中目标时会给发出子弹的生物回复蓝量

将子弹内部逻辑 移动`move()`  绘制`draw()`检测碰撞封装成一个方法`update()`

在每一帧内执行这个`update`方法,该方法返回一个可能存在的碰撞`Collision`

#### 2.6.3 子弹的各种状态

子弹包含各种各样的状态，NONE为普通子弹，其他的各种在碰撞时会给目标生物附加各种状态



### 2.7 包`com.ttf.gourd.collision`

#### 2.7.1 内容

这个包内仅包含一个类`Collision`碰撞类

对于每一个`Collision`对象而言,每当其产生时，就会调用碰撞方法执行碰撞，给目标生物扣血并附加状态，回复攻击者的蓝量



### 2.8 包`com.ttf.gourd.Creature`

#### 2.8.1 内容

这个包内包含三个方面的内容:

1.`Creature`类和`Creature子类`以及产生它们的对象的工厂类`CreatureFactory`

2.`CreatureState`生物状态枚举和`CreatureStateWithClock`生物状态时钟

3.`ImagePosition`

#### 2.8.2 包内类的关系

`Creature`是包括葫芦娃阵营的九个生物和妖精阵营的六个生物的父类

`CreatureFactory`是生产`Creature`子类对象的工厂

15种子类和父类`Creature`的构造器都是默认访问权限,包外的生物对象仅能在工厂内创建,体现了面向对象的封装特性

#### 2.8.3 `Creature`类的设计

 `Creature`类的主要功能被封装在`update()`和`notMyCampUpdate()`两个方法中，分别用来更新本地我的阵营和敌方阵营的生物信息

`Creature`内部的主要功能包括

生物图像绘制`draw()`

生物移动`move()`

生物攻击`ai`对象实现

生物状态信息更新`setCreatureState()`

生物技能实现`QAction(),EAction(),RAction()`

生物信息显示`showMessage()`

......

由于这部分内容过多，这里不详细介绍，具体可见`Creature.java`,此类中有详细注释

#### 2.8.4 `Creature`子类的设计

这里以`SnakeMonster`为例,介绍一下Creature子类的设计方法

```java
public class SnakeMonster extends Creature {
	//数据域:包括技能标志位,技能使用时间,技能效果时长,技能冷却时间,技能属性改变量等信息

    public SnakeMonster(...) {
        super(...)
    }

    @Override
    //重写父类update()方法,加入技能的使用和技能状态的更新
    public ArrayList<Bullet> update() {
        ...
    }
	
    //更新技能状态，技能持续时间是否结束
    private void updateActionState() {
        ...
    }

    //重写父类的QER三项技能
    @Override
    public ArrayList<Bullet> qAction() {
        ...
    }

    @Override
    public void eAction() {
        ...
    }

    @Override
    public void rAction() {
        ...
    }

    //技能失效时调用相关方法
    private void disposeQAction() {
        ...
    }

    private void disposeEAction() {
        ...
    }

    private void disposeRAction() {
        ...
    }
}
```

只要子类的行为方式和父类不相同的地方，就可以重写父类方法

但父类方法没有必要定义成`abstract`因为有些子类会重写它，而另一些不会(不是所有生物都有三个技能)

#### 2.8.5 `CreatureFactory`类的设计

`CreatureFactory`中有两个对外接口

其一是`hasNext()`询问此阵营是否还会有下一个对象

其二是`next()`返回下一个`Creature`对象

通过这个工厂的设计，我们将生物对象和外部分离开来，使它具有良好的封装性

#### 2.8.6 `CreatureStateWithClock`and `CreatureState`

`CreatureState` 是个枚举类型,其中包含了多种多样的生物状态,还包括了生物技能状态

`CreatureStateWithClock`是一个含有时钟的状态类

它可以通过`update()`方法对状态时钟进行实时更新

这个类帮助`Creature`实现了各种`buff`和`debuff`功能，已经这些功能的倒计时,同时也实现了技能的`cd`倒计时

#### 2.8.7 `ImagePosition`类的设计

这个类表示的是一个位置信息,`Creature`和`Bullet`以及`Equipment`都会用到它



### 2.9 包`com.ttf.gourd.Equipment`

#### 2.9.1 内容

这个包内包含了各种装备的信息以及装备生成工厂

#### 2.9.2 各个类的关系

1.`Equipment`是一个抽象类,游戏中存在的5种装备都继承自这个类并且实现了这个类中的抽象方法

2.`EquipmentFactory`是装备对象的生成工厂

#### 2.9.3 `Equipment`抽象类的设计

这个类中有四个较为重要的方法

`draw()`绘制装备,属于各个装备的共有方法

`dispose()`擦除装备,属于各个装备的共有方法

`takeEffect()`装备生效,属于具体装备的方法

`giveUpTakeEffect()`装备移除生效,属于具体装备的方法

因此后两者被设计成抽象方法,`Equipment`被设计成抽象类

与`Creature`不同的是，`Equipment`的每个装备都需要重写`takeEffect()`和`giveUpTakeEffect()`方法,所以将其设计为抽象类

`Creature`类的子类只有部分会重写`update()``QAction()`等方法,所以Creature没有被设计为抽象类

#### 2.9.4 `Equipment`子类设计

针对每个`Equipment`子类,我们重写`takeEffect()`和`giveUpTakeEffect()`方法

在装备生效和装备失效时调用它们



#### 2.9.5 `EquipmentFactory`装备工厂设计

`EquipmentFactory`与`CreatureFactory`的设计原则是一样的

基于实现包内的装备与外部完全分离,将装备的产生完全封装在`EquipmentFactory`中

同是`hasNext()`和`next()`方法,其使用方式并无多大区别

内部实现与思想略有区别

`EquipmentFactory`的`hasNext()`是综合基于对窗口中装备上限显示以及装备显示间隔的考量

`CreatureFactory`的`hasNext()`是因为阵营的生物个数是恒定的





### 2.10 包`com.ttf.gourd.localplayback`

功能：这个包主要用于本地回放。

代码分析：

- `PlayBackFile.java`主要用于存储本地回放文件的相关信息；
- `LoadPlayBackFiles.java`主要用于显示本地回放文件，添加本地回放文件到list中，通过选择本地回放文件进行回放；
- `GamePlayBack.java`，关键文件，用于初始化回放中的资源，以及接收本地回放文件中协议的类型，然后调用`ContentParse.java`文件进行内容解析，根据解析得到的信息来设置界面上的图片信息以及葫芦娃，子弹和装备的状态信息。

```java
// GamePlayBack.java文件
public class GamePlayBack {
	// 首先初始化资源信息
    public void initGame() {
    }

    public void playBackGame() {
        initGame();
        init();
        // 下面是在一个新的线程里进行工作的，每次循环都会sleep一帧的时间，这个时间是可以通过倍速进行修改的。
        // ...
        // 用于解析回放文件中的信息
        while(true) {
            int contentType = inputStream.readInt();
            // 第一个if，是原游戏的一帧结束，即break，在下面进行设置各项资源，然后继续解析
            if(contentType == Msg.FRAME_FINISH_FLAG_MSG) break;
            else if(contentType == Msg.FINISH_GAME_FLAG_MSG) {
                gameOverFlag = true;
                gameOver(Constant.gameOverState.VICTORY);
                inputStream.close();
                break;
            }
            else contentParse.parsePlayBackContent(inputStream, contentType);
        }
        // ...
    }

    // 这个函数主要就是初始化界面以及绑定一些函数的地方
    private void init() {
    }

    // 这个函数主要是用于检测到游戏结束后，播放结束的一个图片，然后任意点击屏幕即可退出回放界面
    private void gameOver(int gameOverState) {
    }
}
```



### 2.11 包`com.ttf.gourd.constant`

#### 2.11.1 内容

这个包中包含了游戏过程中各种常量信息,以及很多静态数据

1.各种长度信息,窗口大小信息等

2.各种时间信息,帧时间信息,`ai`方向锁定时间信息等

3.各种常量参数,胜败状态，方向状态，爪痕状态等

4.各种生物及装备的`ID`

5.静态加载的图片信息



### 2.12 包`com.ttf.gourd.tool`

功能：主要是作为工具类使用



## 3 游戏测试

### 3.1 单元测试

#### 3.1.1 测试通信中的输入输出的协议是否能够正确解析

**准备**：随机生成一个信息种类，然后根据信息种类随机生成相应的信息，然后保存到本地，与此同时将该信息存放到一个`ArrayList`中。

**测试**:通过读入保存到本地的测试文件，根据协议进行协议，即通过不同的消息种类进行不同的解析，然后与`ArrayList`存储的信息进行比较。

**结果**：完全一致，说明协议相关的类和函数从测试来看是不存很大问题的。

### 3.2 胜率测试

#### 3.2.1 预期目标

由于葫芦娃阵营的生物较多，人为操控技能释放难度高于妖精阵营，故而预期要达到纯`ai`操作胜率六四开，葫芦娃6，妖精4

#### 3.2.2 游戏测试数据

当前数据下共进行了511局,妖精胜利213局,葫芦娃胜利298局,双方都可以使用技能，都可以拾取装备,完全`ai`控制

妖精胜率41.68%,葫芦娃胜率58.32%，几乎符合预期

#### 3.2.3 游戏测试文件

游戏测试文件在`jlog`文件夹下，此数据是在单机情况下测试的

详细测试代码请看`github`地址 [https://github.com/JansonSong/gourd_vs_monster/tree/dev/gourd_vs_monster/src/main/java/com/sjq/gourd/localtest ](https://github.com/JansonSong/gourd_vs_monster/tree/dev/gourd_vs_monster/src/main/java/com/sjq/gourd/localtest)

## 4 开发过程

### 4.1 需求分析与策划

耗时约`1day`

### 4.2 模块划分与任务分配

耗时约`1day`

### 4.3 单机版实现

耗时约`12days` 详见 [https://github.com/JansonSong/gourd_vs_monster/tree/dev](https://github.com/JansonSong/gourd_vs_monster/tree/dev)

### 4.4 联机同步实现

耗时约`8days` 详见 [https://github.com/JansonSong/gourd_vs_monster](https://github.com/JansonSong/gourd_vs_monster)

### 4.5 功能和文档完善

耗时约`3days`

### 4.6 游戏设计分工

宋鉴清：游戏策划，界面设计，图片的PS，网络通信，远程同步，本地回放，单元测试等功能

项仁浩：游戏单机版的实现，生物类、子弹类、技能装备等设计，单机游戏设计等功能。

功能完成README.md的编写。



## 5 写给玩家

### 5.1 玩法说明

游戏分为两个阵营,葫芦娃阵营和妖精阵营，葫芦娃阵营有9个生物，妖精阵营有6个生物

玩家每个时刻最多只能操控唯一的生物,其他生物由`ai`控制

当玩家为**葫芦娃阵营**时，可通过主键盘数字键1~9进行对操控目标的选取

当玩家为**妖精阵营**时,可通过主键盘数字键1~6进行对操控目标的选取

操控生物选取完毕后,屏幕侧面会显示你当前操控生物的各项信息,你可以通过`WSAD`键进行上下左右的移动

你可以通过鼠标左键点击敌方目标进行攻击,一旦选取敌方目标,当前操控生物就会自动对选定的敌方目标进行攻击(无须反复点击,只要在攻击范围内就会自动攻击),并且此时敌方目标的信息会被显示在侧边栏

一旦你通过数字键更换了当前操控生物,此生物就会自动由`ai`接管

**葫芦娃阵营**除了穿山甲之外都有且仅有一个技能Q

**妖精阵营**蛇精和蝎子精都有`QER`三个技能，其他小妖精只有R技能



**葫芦娃阵营** 大娃，二娃，三娃，四娃，五娃，六娃，七娃，爷爷，穿山甲

**妖精阵营** 蛇精，蝎子精，蜈蚣精，蝙蝠精，鳄鱼精，蛤蟆精



两个玩家，双方各自控制一方，使用主键盘数字键选择控制生物，对于每个玩家而言，己方阵营的每个生物有着唯一的控制编号



### 5.2 葫芦娃阵营详细数据

| 控制编号 | 名称   | 攻击类型 | 攻击效果 | 基础生命 | 基础魔法 | 基础攻击 | 基础防御 | 基础攻速 | 基础移速 | 攻击范围 | 技能及其描述                                                 |
| -------- | ------ | -------- | -------- | -------- | -------- | -------- | -------- | -------- | -------- | -------- | ------------------------------------------------------------ |
| 1        | 大娃   | 近战     | 爪痕1    | 3500     | 100      | 120      | 40       | 0.5      | 10       | 80       | 技能Q(能屈能伸)：消耗所有蓝量,变成原来的一半大小,增加移速5,降低攻击力20,攻击范围增加100，持续五秒,该技能五秒内最多使用一次 |
| 2        | 二娃   | 远程     | 橙色子弹 | 3000     | 150      | 80       | 35       | 0.5      | 10       | 400      | 技能Q(振奋人心):消耗所有蓝量,给2.0*当前攻击范围的范围内的所有友军增加时长五秒的振奋buff,该技能20秒内最多使用一次 |
| 3        | 三娃   | 近战     | 爪痕1    | 4500     | 100      | 150      | 60       | 0.4      | 10       | 80       | 技能Q(无敌金身):消耗所有蓝量,大幅度提高攻击力(50)和防御力(100)，移速减少3,若移速不足3,减少到0,持续五秒,该技能五秒内最多使用一次 |
| 4        | 四娃   | 远程     | 红色子弹 | 3000     | 150      | 80       | 40       | 0.5      | 12       | 400      | 技能Q(火焰之神的降临):消耗所有的蓝量,给1.5*当前射程范围内的所有敌军发射一颗带有"火焰之子"特效的子弹,当该子弹命中敌人之后,造成瞬间伤害与四娃普通子弹伤害相同，并给目标附加3秒钟的灼烧效果,该技能没有CD限制,蓝满即可释放 |
| 5        | 五娃   | 远程     | 蓝色子弹 | 3000     | 150      | 80       | 40       | 0.5      | 12       | 400      | 技能Q(冰霜之神的降临):消耗所有的蓝量,给1.5*当前射程范围内的所有敌军发射一颗带有"冰霜之心"特效的子弹,当该子弹命中敌人之后,造成瞬间伤害与五娃普通子弹伤害相同，并给目标附加2秒钟的冰冻效果,该技能没有CD限制,蓝满即可释放 |
|          | 六娃   | 近战     | 爪痕1    | 3500     | 100      | 150      | 20       | 1.0      | 12       | 90.0     | 技能Q(你看不见我):消耗所有蓝量,隐身,移速提升5，攻击力降低20,防御力提升100,该技能持续5秒,5秒内最多使用一次 |
|          | 七娃   | 远程     | 紫色子弹 | 2500     | 100      | 80       | 20       | 0.9      | 15       | 600      | 技能Q(看我法宝):5秒内提升基础攻速的300%，攻击范围提升400,5秒内最多使用一次 |
| 8        | 爷爷   | 远程     | 绿色子弹 | 3000     | 200      | 75       | 10       | 0.5      | 12       | 300      | 被动(山神的庇佑)：爷爷不会以妖精为攻击目标,只会以葫芦娃为治疗目标,每颗普通子弹都带有“治愈之神”的特效,每颗子弹的治疗值为爷爷的攻击力<br />技能Q(山神的祝福):爷爷对1.5*当前射程范围内的所有友军发射一颗带有“超大号治愈之神”特效的子弹,命中时对目标治疗1000生命值,并使得目标获得时长5秒的治愈buff |
| 9        | 穿山甲 | 近战     | 爪痕1    | 3000     | 100      | 75       | 60       | 0.5      | 12       | 100.0    | 没有技能                                                     |

### 5.3 妖精阵营详细数据

| 控制编号 | 名称   | 攻击类型 | 攻击效果 | 基础生命 | 基础魔法 | 基础攻击 | 基础防御 | 基础攻速 | 基础移速 | 攻击范围 | 技能及其描述                                                 |
| -------- | ------ | -------- | -------- | -------- | -------- | -------- | -------- | -------- | -------- | -------- | ------------------------------------------------------------ |
| 1        | 蛇精   | 远程     | 黑色子弹 | 5000     | 200      | 120      | 30       | 0.7      | 15       | 500      | Q技能(剧毒之牙):消耗50%最大蓝量,向2.0*攻击范围内的所有敌方放射一颗带有“剧毒之牙”特效的子弹,命中时对目标造成等同蛇精普通子弹的伤害,并附加“重伤”效果，该技能每五秒只能使用一次<br />E技能(复活之术):消耗100%最大蓝量,如果己方有妖精死掉了,就复活一只妖精,复活优先级：蝎子精>蜈蚣精>蝙蝠精>鳄鱼精>蛤蟆精。同样死掉的情况下,复活优先级高的,由该技能复活的妖精蓝量为0，蝎子精血量为满血的一半,其他妖精血量为满血,妖精复活位置为其上一次死掉的位置。如果没有妖精死亡,则视为空技能,只扣蓝量,没有效果，该技能每五秒只能使用一次<br />R技能(金身护罩)：消耗50%最大蓝量,瞬间加600血量,在五秒内防御力提升30,该技能每五秒只能使用一次 |
| 2        | 蝎子精 | 近战     | 爪痕3    | 7500     | 150      | 150      | 55       | 0.5      | 10       | 100      | Q技能(致残之爪):消耗100%最大蓝量,对5.0*当前近战攻击范围内的所有敌方进行致残爪击，并在爪击命中时给目标附加“残废”效果,效果持续5秒,该技能五秒内只能使用一次<br />E技能(狂暴之心):消耗50%最大蓝量,在五秒内,移速增加10,防御增加20，攻击增加30，攻速增加100%基础攻速,攻击范围增加80，血量增加600,该技能每五秒只能使用一次<br />R技能(同命)：与消耗100%最大蓝量,在效果持续的10秒内,与敌方血量最低的固定(不会实时变动)三个生物绑定,每当蝎子精掉血时,绑定生物掉相同血量,该技能每10秒只能使用一次 |
| 3        | 蜈蚣精 | 近战     | 爪痕4    | 3500     | 100      | 75       | 30       | 0.5      | 8        | 80       | R技能(复活吧!女王大人)：触发条件1.蛇精死掉了 2.四个小怪全部活着 3.四个小怪蓝量都是满的 4.按下R键<br />效果:四个小怪消耗全部的当前血量和全部的所有蓝量,死亡后复活蛇精,蛇精复活时满血满蓝 |
| 4        | 蝙蝠精 | 远程     | 褐色子弹 | 3500     | 100      | 70       | 15       | 1.0      | 18       | 600      | 同上                                                         |
| 5        | 鳄鱼精 | 近战     | 爪痕2    | 3500     | 100      | 100      | 50       | 0.5      | 8        | 80       | 同上                                                         |
| 6        | 蛤蟆精 | 远程     | 黄色子弹 | 3500     | 100      | 75       | 40       | 0.5      | 10       | 500      | 同上                                                         |

### 5.4 装备详细数据

| 装备名称   | 装备效果                                                     | 装备备注 |
| ---------- | ------------------------------------------------------------ | -------- |
| 玉如意     | 所有生物都可以拾取,所有生物都可以使用<br />对葫芦娃而言是增加8移速,增加20攻击力<br />对于妖精而言是增加20防御,并瞬间恢复1000生命值 |          |
| 玉簪       | 所有生物都可以拾取,但只有蛇精才可以使用<br />蛇精拾取瞬间恢复1500血量，增加5移速，增加200%基础攻速,增加攻击力20,增加防御力20<br />其他生物可以拾取，但没有作用 |          |
| 魔镜       | 对于任何远程攻击生物,都可拾取并装备,增加远程攻击射程200<br />对葫芦娃方生物有额外效果,可在拾取瞬间恢复500生命值<br />近战生物可拾取但无装备效果 |          |
| 刚柔阴阳剑 | 任何生物都可以拾取并装备，增加基础攻速100%,增加攻击力20      |          |
| 百宝锦囊   | 任何生物都可拾取，但无法装备,效果是拾取瞬间恢复500生命值     |          |



## 6 总结和感想

这个项目从开始布置，到完成为止，一直在编写，但是依然有很多想实现而没有实现的功能，并且程序的鲁棒性有待提高，主要表现在，对文件路径以及操作等正确性的要求比较高，
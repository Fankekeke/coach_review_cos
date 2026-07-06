![](https://img.shields.io/badge/License-MIT-blue.svg)

![](https://img.shields.io/badge/Version-1.0.0-brightgreen.svg)

![](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

# 教练员工在线考核平台 (Coach Assessment Platform)

**AI驱动的一站式教练团队能力评估与成长平台**

教练员工在线考核平台是一个集**智能评估、在线考核、能力画像、实时互动**于一体的综合性SaaS系统。平台通过AI智能建议规划、自动评分、多维度能力画像等核心功能，帮助管理员全面掌握教练团队的综合能力分布，同时为员工提供个性化的学习路径与成长反馈，打造高效、透明、数据驱动的人才培养闭环。

#### 安装环境
**️ 注意**：请确保您的开发环境已安装以下依赖。

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 一定要把账户和密码记住

redis

Idea 编译器

WebStorm OR VScode 编译器

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok

---

## 核心特性

- **AI 驱动的智能评估**：告别传统单一的人工评价，平台深度融合 AI 智能建议规划与自动评分算法。不仅能秒级处理客观题评分，更能基于多维数据自动生成个人能力画像，实现从“经验评估”向“数据驱动”的跨越。
- **沉浸式实时互动生态**：突破传统考核平台的孤岛效应，创新性地将“在线房间讨论”、“会议成员评分”与“问答社区”深度整合。支持高并发的实时音视频互动与弹幕/点赞反馈，让知识沉淀在交流中自然发生。
- **全景式人才能力图谱**：通过沉淀考核数据、互动投票、答题记录与社区表现，系统能够动态生成各方向综合能力对比与方向分布统计。为管理者提供上帝视角，精准识别团队能力短板与高潜人才。
- **灵活可配置的考核引擎**：内置高度灵活的题库管理与评估任务配置中心。无论是专业方向考核还是候选人能力筛查，均可自定义评分规则与任务流转节点，完美适配企业复杂多变的内训与考核场景。

---

## ️ 功能模块概览

### ️ 管理员端：全局统筹与数据洞察

- **多维数据看板**：提供各方向综合能力对比、各维度平均分对比、方向分布统计及能力画像统计，帮助管理者全局掌握团队人才梯队现状。
- **考核与评估中心**：支持评估任务的灵活发布、候选人能力评估及教练管理。涵盖答题记录追踪、任务评分提交以及客观题自动评分，实现考核流程的数字化与标准化。
- **互动社区管理**：支持创建与管理讨论房间（包括发起会议、查看会议详情、会议成员评分），并提供问题答疑、顶踩投票及公告信息推送功能，活跃组织内部交流氛围。
- **系统与资源管理**：提供题库信息维护、消息通知下发及管理员权限管理，保障平台底层数据的安全与高效运转。

### 员工/教练端：智能学习与自我驱动

- **AI 赋能成长**：内置 AI 智能建议规划与能力画像生成，结合自我评估与学习/知识问答库，为员工量身定制个性化学习路径。
- **在线考核系统**：支持考核任务提交、在线评估答题及答题自动评分。员工可随时查看答题记录，清晰掌握自身知识盲区。
- **实时互动社区**：打造专属问答社区与讨论大厅。支持发布问题、回复互动、点赞/踩、内容顶踩投票，并可随时进入或加入在线房间进行实时讨论。
- **个人中心**：提供便捷的邮箱注册、个人信息维护、公告查看、我的投票记录追踪及系统消息通知接收，提供一站式个人工作台体验。

---

## 使用指南

### 管理员工作流

1. **创建题库** → 在「题库信息」中录入各方向考核题目
2. **发布评估任务** → 设定考核时间、参与人员、评分规则
3. **查看数据看板** → 实时查看各方向能力对比与维度平均分
4. **管理讨论房间** → 发起在线会议，查看成员评分与互动数据
5. **推送公告** → 向全员或指定方向发送通知

### 员工/教练工作流

1. **注册登录** → 使用邮箱注册，完善个人信息
2. **完成自我评估** → 获取AI生成的能力画像与成长建议
3. **参加在线考核** → 答题后系统自动评分，记录答题历史
4. **参与社区互动** → 在问答社区提问/回答，在讨论大厅参与实时讨论
5. **查看成长轨迹** → 通过能力画像追踪个人能力提升趋势

## 项目结构

```text
pharmacy-dispensing-system/
├── backend/            # 后端 API 服务
│   ├── src/
│   │   ├── controllers/  # 控制器
│   │   ├── models/       # 数据模型
│   │   ├── routes/       # 路由
│   │   └── services/     # 业务逻辑
│   └── package.json
├── frontend/           # 前端 Web 应用
│   ├── src/
│   │   ├── views/      # 页面视图
│   │   ├── components/ # 公共组件
│   │   └── store/      # 状态管理
│   └── package.json
├── docs/               # 项目文档与 API 接口说明
├── LICENSE
└── README.md
```
---


#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 默认后台账户密码
[管理员]
admin
123456

[教练]
fank
123456

#### 项目截图

|  |  |
|---------------------|---------------------|
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/033e5e7b-c971-4b0f-acac-7c684ffae157.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b75792d0-90b0-49ce-bb81-f41e724109d7.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/26a1df96-aecb-44c0-90da-04491d337b9f.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b06802d9-49b9-45ed-a9ba-87d2690155ab.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/6a7a3352-c1f2-4bad-9f1f-481e1d6ca9d0.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b55ab412-bc8d-463e-bc8e-d072ad8f6b1c.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/5d4cc9f1-0760-4132-be45-79211434ab27.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b4c6eb17-27a5-466b-8093-8b4e852362f8.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/5b6935bb-cf89-4746-a7f6-6e0c5e535f50.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/a0db0448-ee2c-4a0d-9879-b23eb3df639e.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/5b17a225-9409-46da-bfde-428f2e1cbf59.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/604836c5-d82e-4e2d-a2e3-900ba564d635.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/5a0fa03c-3a32-42c5-af4a-52fea27ab832.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/9055f1bc-7414-486f-87f7-767a27e4a9bb.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/2fd0ddfe-fb16-4f58-a932-511985e29540.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/5084a4ad-cbe6-46ad-adc0-26b9e20a7db5.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/2dbe65d9-f982-4f81-a170-4b68baba854f.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/2334e9e7-7718-453d-92d1-c4ee95c85f29.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1bb0eb2d-f5cd-483a-8c36-c466e5dd4440.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/868de90a-c7a8-4f95-9190-7d4bcbc33ac1.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/0fbeb878-82ba-432b-ae4b-21b047769571.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/585cb42b-407b-410d-bb0a-1670594e44e9.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/fd87a70f-1c0c-483b-a6ba-bec9f9706d20.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/415ba80a-766e-4ad4-ab22-73074f01757e.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/f7425e23-9537-4fad-a267-6b8159a29bce.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/362b314e-0e22-445c-a49d-5f0f27a71fb3.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/f5bdf2ee-0e9c-437a-9565-26d4c1e68935.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/97fe9b68-f37a-498f-afc2-5d4ac4dcb93f.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/dfdff459-bc1b-4e2d-8812-935533776a20.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/39ad97b9-9ade-4964-9b60-2a21045da121.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/cc582cb2-2129-47a8-ad90-899ed65ddc10.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/34a8e838-07e6-430f-9251-d87ad30afae4.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/c7b07a10-c022-48eb-bba3-f29b1b02e595.png) |  |

#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解😭`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.**黑奴价格**

> 项目部署调试不好包退！功能逻辑没讲明白包退！

#### 其它资源
[2026年-答辩顺利通过-客户评价🀄](https://berserker287.github.io/2026/06/29/2026%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2025年-答辩顺利通过-客户评价🍜](https://berserker287.github.io/2025/06/18/2025%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2024年-答辩顺利通过-客户评价👻](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年-答辩顺利通过-客户评价🐢](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年-答辩通过率100%-客户评价🐣](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)

#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)

<p><img align="center" src="https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/%E5%90%88%E4%BD%9C%E7%89%A9%E6%96%99%E6%A0%B7%E5%BC%8F%20(3).png" alt="fankekeke" /></p>
****

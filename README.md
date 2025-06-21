# 供应链管理SaaS系统

一个现代化的多端供应链管理SaaS平台，支持Web端、Android员工端和Android管理端，集成SiliconFlow AI功能。

## 🌍 多语言与国际化支持

- 支持中英文双语界面，Web端、Android员工端、Android管理端均已实现多语言切换。
- 移动端（员工端/管理端）已上线英文资源文件，支持实时切换语言。
- 登录界面可一键切换中英文，切换后界面即时生效，且会自动记忆用户偏好。
- 所有主要功能模块均已本地化，后续将持续完善React Native部分的多语言支持。

## 🤖 AI能力集成

- 已集成 SiliconFlow 平台，支持 Qwen2.5-72B、DeepSeek-R1 等大模型。
- AI能力包括：智能路线规划、KPI自动追踪、业绩预测分析、语音助手、图像识别等。
- AI能力已在Web端和移动端部分场景上线。

## 项目架构

```
supply-chain-saas/
├── web/                    # Web端项目 (React + TypeScript + Vite)
├── mobile-employee/        # Android员工端 (React Native)
├── mobile-manager/         # Android管理端 (React Native)
├── shared/                 # 多端共享代码
├── packages/              # 公共包
├── backend/               # 后端API (Node.js)
├── database/              # 数据库相关
└── docs/                  # 项目文档
```

## 技术栈

### Web端
- **框架**: React 18 + TypeScript
- **构建工具**: Vite
- **状态管理**: Redux Toolkit
- **样式**: Tailwind CSS
- **UI组件**: Headless UI

### 移动端
- **框架**: React Native
- **UI库**: NativeBase
- **导航**: React Navigation
- **原生功能**: 扫码、拍照、地图、生物识别

### 后端
- **运行时**: Node.js
- **框架**: Express.js
- **数据库**: MongoDB/PostgreSQL
- **认证**: JWT

### AI功能
- **平台**: SiliconFlow
- **模型**: Qwen2.5-72B、DeepSeek-R1等
- **功能**: 路线规划、KPI追踪、预测分析

## 快速开始

### 环境要求

- Node.js >= 16
- npm >= 8
- React Native CLI (移动端开发)
- Android Studio (Android开发)

### Web端开发

```bash
cd web
npm install
npm run dev
```

访问 http://localhost:3000

### 移动端开发

```bash
cd mobile-employee  # 或 mobile-manager
npm install
npx react-native run-android
```

### 后端开发

```bash
cd backend
npm install
npm run dev
```

## 功能模块

### Web端功能
- ✅ 用户认证系统
- ✅ 响应式仪表盘
- 🚧 产品管理
- 🚧 订单管理
- 🚧 库存管理
- 🚧 供应商管理
- 🚧 客户管理
- 🚧 财务管理
- 🚧 报表分析
- 🚧 AI自动化监视器

### Android员工端功能
- 📋 移动工作流
- 📋 扫码操作
- 📋 拍照记录
- 📋 AI路线规划
- 📋 离线同步
- 📋 语音助手

### Android管理端功能
- 📋 移动监控
- 📋 实时报表
- 📋 AI管理驾驶舱
- 📋 推送通知
- 📋 地图查看

### AI功能
- 📋 智能路线规划
- 📋 KPI自动追踪
- 📋 业绩预测分析
- 📋 决策支持系统
- 📋 语音交互
- 📋 图像识别

## 开发进度

### 第1周 - 项目初始化 (已完成 ✅)
- [x] 多端项目架构搭建
- [x] Web端基础框架 (React + Vite + TypeScript)
- [x] Tailwind CSS配置
- [x] Redux状态管理
- [x] 路由配置
- [x] 基础组件和页面

### 第2-3周 - 认证和基础功能
- [ ] 移动端项目初始化
- [ ] 用户认证系统完善
- [ ] 数据库设计
- [ ] API接口设计
- [ ] 基础AI功能框架

### 第4-5周 - 核心业务模块
- [ ] 产品管理模块
- [ ] 订单管理模块
- [ ] 库存管理模块
- [ ] AI路线规划功能

### 第6-8周 - 高级功能
- [ ] 供应商和客户管理
- [ ] 财务管理模块
- [ ] AI KPI追踪功能
- [ ] 移动端特色功能

### 第9-11周 - AI集成和优化
- [ ] SiliconFlow AI完整集成
- [ ] AI预测分析功能
- [ ] AI决策支持系统
- [ ] 性能优化

### 第12-13周 - 系统集成和测试
- [ ] 多端数据同步
- [ ] 全面测试
- [ ] 部署配置
- [ ] 文档完善

## 部署

### Web端部署
```bash
cd web
npm run build
# 部署 dist 目录到服务器
```

### 移动端打包
```bash
cd mobile-employee
npx react-native run-android --variant=release
```

## 贡献指南

1. Fork项目
2. 创建功能分支: `git checkout -b feature/新功能`
3. 提交变更: `git commit -am '添加新功能'`
4. 推送分支: `git push origin feature/新功能`
5. 创建Pull Request

## 许可证

MIT License

## 联系方式

如有问题或建议，请提交Issue或联系项目维护者。 
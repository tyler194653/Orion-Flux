# 供应链管理SaaS系统 - Web端

基于React + TypeScript + Vite构建的现代化供应链管理Web应用。

## 技术栈

- **框架**: React 18 + TypeScript
- **构建工具**: Vite
- **状态管理**: Redux Toolkit
- **路由**: React Router DOM
- **样式**: Tailwind CSS
- **UI组件**: Headless UI
- **图标**: Heroicons
- **表单**: React Hook Form + Zod
- **图表**: Chart.js + Recharts
- **HTTP客户端**: Axios
- **国际化**: React i18next

## 项目结构

```
web/
├── public/                 # 静态资源
├── src/
│   ├── components/         # 通用组件
│   │   ├── common/         # 通用组件
│   │   ├── layout/         # 布局组件
│   │   ├── forms/          # 表单组件
│   │   ├── charts/         # 图表组件
│   │   └── ai/             # AI功能组件
│   ├── pages/              # 页面组件
│   │   ├── auth/           # 认证页面
│   │   ├── dashboard/      # 仪表盘
│   │   ├── products/       # 产品管理
│   │   ├── orders/         # 订单管理
│   │   ├── inventory/      # 库存管理
│   │   └── suppliers/      # 供应商管理
│   ├── hooks/              # 自定义Hooks
│   ├── services/           # API服务
│   ├── store/              # Redux状态管理
│   │   └── slices/         # Redux切片
│   ├── utils/              # 工具函数
│   ├── config/             # 配置文件
│   └── assets/             # 静态资源
├── .env.example            # 环境变量示例
└── package.json            # 项目依赖
```

## 开发指南

### 环境要求

- Node.js >= 16
- npm >= 8

### 开始开发

1. 安装依赖
```bash
npm install
```

2. 复制环境变量文件
```bash
cp .env.example .env
```

3. 启动开发服务器
```bash
npm run dev
```

4. 访问 http://localhost:3000

### 可用脚本

- `npm run dev` - 启动开发服务器
- `npm run build` - 构建生产版本
- `npm run preview` - 预览构建结果
- `npm run lint` - 运行ESLint检查
- `npm run lint:fix` - 自动修复ESLint问题
- `npm run type-check` - TypeScript类型检查

## 功能特性

### 已实现功能

- ✅ 项目初始化和基础架构
- ✅ 用户认证系统
- ✅ 响应式布局设计
- ✅ 状态管理(Redux)
- ✅ 路由配置
- ✅ 基础仪表盘

### 开发中功能

- 🚧 产品管理模块
- 🚧 订单管理模块
- 🚧 库存管理模块
- 🚧 供应商管理模块
- 🚧 AI功能集成

### 计划功能

- 📋 财务管理模块
- 📋 报表分析模块
- 📋 系统设置模块
- 📋 SiliconFlow AI集成
- 📋 多语言支持

## 开发规范

### 代码风格

- 使用TypeScript严格模式
- 遵循ESLint配置
- 使用Prettier格式化代码
- 组件使用函数式组件 + Hooks

### 命名规范

- 组件文件使用PascalCase：`UserProfile.tsx`
- Hook文件使用camelCase：`useAuth.ts`
- 工具函数使用camelCase：`formatDate.ts`
- 常量使用UPPER_SNAKE_CASE：`API_BASE_URL`

### 目录规范

- 每个功能模块创建独立目录
- 组件按功能分类存放
- 公共组件放在common目录
- 页面组件放在pages目录

## 部署

### 构建

```bash
npm run build
```

构建产物将输出到 `dist` 目录。

### 环境变量

生产环境需要配置以下环境变量：

- `VITE_API_BASE_URL` - 后端API地址
- `VITE_SILICONFLOW_API_KEY` - SiliconFlow API密钥

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交变更
4. 推送到分支
5. 创建Pull Request

## 许可证

MIT License 
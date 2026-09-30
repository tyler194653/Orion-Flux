# 供应链员工端 - 混合开发方案 v1.0.0

这是一个使用原生 Android (Kotlin) + React Native (TypeScript) 混合开发的供应链员工端应用。

---

## 🚀 项目概述

本项目采用混合开发架构，结合原生 Android 在硬件交互（相机、生物识别、定位）与系统集成方面的性能优势，以及 React Native 在跨平台 UI 开发中的高效体验，为供应链员工提供完整、稳定且高效率的工作工具。

---

## 📱 技术栈

### 🤖 原生 Android
- **开发语言**: Kotlin 2.0.21
- **构建工具**: Gradle 8.11.1 (Android Gradle Plugin 8.10.1)
- **SDK 配置**: `compileSdk = 35`, `targetSdk = 35`, `minSdk = 26` (Android 8.0+)
- **架构组件**: Jetpack Navigation Component, ViewModel, LiveData, ViewBinding
- **硬件与核心库**:
  - **相机与扫码**: CameraX (1.3.1), ZXing Embedded (4.3.0) / ZXing Core (3.5.2)
  - **生物识别**: AndroidX Biometric (1.1.0)
  - **网络请求**: Retrofit (2.9.0), OkHttp3 (4.12.0), Gson (2.10.1)
  - **图片加载**: Glide (4.16.0)
  - **权限管理**: PermissionX (1.7.1)
  - **异步并发**: Kotlin Coroutines (1.7.3)

### ⚛️ React Native
- **开发语言**: TypeScript
- **构建 & 打包**: Metro Bundler (1.3.0)
- **导航组件**: BottomTabNavigator
- **混合集成**: React Native Fragment + Bridge 架构

---

## 🏗️ 项目结构

```text
mobile-employee-simple/
├── app/                                              # Android 原生模块
│   ├── build.gradle.kts                              # 应用层 Gradle 构建配置
│   ├── proguard-rules.pro                            # ProGuard 混淆规则
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml                   # 应用清单文件
│       │   ├── java/com/example/mobile_employee_simple/
│       │   │   ├── MainActivity.kt                   # 主 Activity (处理 Navigation、侧边栏及语言切换)
│       │   │   ├── MobileEmployeeApplication.kt      # Application 入口类
│       │   │   ├── services/                         # 后台服务与工具
│       │   │   │   ├── BiometricService.kt           # 生物识别 / 指纹认证服务
│       │   │   │   ├── CameraService.kt              # 相机调用与图片处理服务
│       │   │   │   ├── LocationService.kt            # GPS 定位与权限处理服务
│       │   │   │   ├── NotificationService.kt        # 本地与推送通知服务
│       │   │   │   └── SyncService.kt                # 离线数据缓存与同步服务
│       │   │   └── ui/                               # 原生 UI 模块
│       │   │       ├── auth/                         # 用户认证 (LoginFragment, LoginViewModel)
│       │   │       ├── camera/                       # 相机与扫码 (CameraFragment, CameraViewModel)
│       │   │       ├── gallery/                      # 产品图鉴 (GalleryFragment, GalleryViewModel)
│       │   │       ├── home/                         # 主页 (HomeFragment, HomeViewModel)
│       │   │       ├── inventory/                    # 库存管理 (InventoryFragment, InventoryViewModel, InventoryAdapter)
│       │   │       ├── react/                        # React Native 容器 (ReactNativeFragment)
│       │   │       ├── settings/                     # 系统设置与弹窗 (SettingsFragment, SettingsViewModel, About/Help Dialog)
│       │   │       ├── slideshow/                    # 多媒体展示 (SlideshowFragment, SlideshowViewModel)
│       │   │       ├── user/                         # 个人信息 (UserInfoFragment)
│       │   │       └── workbench/                    # 原生工作台 (WorkbenchFragment)
│       │   └── res/                                  # 资源文件
│       │       ├── layout/                           # XML 布局定义
│       │       ├── menu/                             # 侧边栏与 Toolbar 菜单项
│       │       ├── navigation/                       # Jetpack Navigation 导航图 (mobile_navigation.xml)
│       │       ├── values/                           # 默认资源 & 中文文本 (strings.xml, colors.xml, themes.xml)
│       │       └── values-en/                        # 英文语言包 (strings.xml)
│       ├── androidTest/                              # Android UI & 集成测试
│       └── test/                                     # 原生单元测试 (JUnit 4)
├── src/                                              # React Native 源码模块
│   ├── App.tsx                                       # React Native 主入口
│   ├── navigation/
│   │   └── BottomTabNavigator.tsx                    # RN 底部标签栏导航
│   └── screens/                                      # React Native 业务界面
│       ├── AIWorkAssistantScreen.tsx                 # AI 工作助手界面
│       ├── ApprovalsScreen.tsx                       # 审批流程界面
│       ├── ProfileScreen.tsx                         # 个人中心界面
│       ├── ReportsScreen.tsx                         # 数据报表统计界面
│       ├── TaskListScreen.tsx                        # 任务列表界面
│       └── WorkbenchScreen.tsx                       # RN 混合工作台界面
├── gradle/
│   ├── libs.versions.toml                            # Gradle 版本目录 (Dependencies Version Catalog)
│   └── wrapper/                                      # Gradle Wrapper 文件
├── build.gradle.kts                                  # 顶层 Gradle 构建脚本
├── settings.gradle.kts                               # 项目与仓库设置脚本
├── gradle.properties                                 # Gradle 全局属性配置
├── index.js                                          # Metro 打包入口文件
├── metro.config.js                                   # Metro Bundler 配置文件
├── package.json                                      # Node.js 依赖与脚本配置
└── tsconfig.json                                     # TypeScript 编译配置
```

---

## ✨ 核心功能模块

### 🔐 1. 认证与安全 (Auth & Security)
- **登录管理**: `LoginFragment` + `LoginViewModel` 管理登录状态持久化。
- **生物识别**: 通过 `BiometricService` 支持指纹及面部身份验证。
- **多语言登录**: 支持登录界面中文/英文实时切换与持久化偏好。

### 🧭 2. 导航与主框架 (Navigation Framework)
- **侧边栏导航**: `DrawerLayout` + `NavigationView` 组合，覆盖首页、工作台、相机、库存、图鉴、设置及个人信息。
- **动作栏集成**: 顶部 `Toolbar` 菜单提供用户信息与设置快捷入口。
- **路由控制**: 使用 `Navigation Component` 管理路由栈与登录状态感知导航。

### 📊 3. 工作台与业务 (Workbench & Operations)
- **原生工作台**: `WorkbenchFragment` 提供任务列表、库存管理、报表统计等快捷入口。
- **RN 混合工作台**: 通过 `ReactNativeFragment` 无缝嵌入 React Native 编写的工作台及 AI 助手。

### 📷 4. 相机与扫码 (Camera & Scanner)
- **实时预览与拍照**: 基于 `CameraX` API 实现高质量相机预览与照片捕获。
- **条码/二维码解析**: 集成 `ZXing` 库实现条形码与二维码的扫描解析。

### 📦 5. 库存与产品图鉴 (Inventory & Gallery)
- **库存管理**: `InventoryFragment` + `InventoryAdapter` 提供高效列表渲染与物品搜索筛选。
- **产品图鉴**: `GalleryFragment` 展示产品多图与详细规格信息。

### ⚙️ 6. 系统设置与多语言 (Settings & i18n)
- **系统设置**: 支持偏好设置、离线模式切换、版本说明与帮助文档弹窗 (`AboutDialogFragment`, `HelpDialogFragment`)。
- **多语言国际化**: 完整支持**中文（简体）**与**英文**，提供系统语言检测与界面实时切换。

### 🛠️ 7. 后台服务与数据同步 (Services & Sync)
- **位置服务 (`LocationService`)**: GPS 实时定位与权限申请管理。
- **通知服务 (`NotificationService`)**: 本地消息通知与推送通道管理。
- **离线同步 (`SyncService`)**: 支持本地 JSON 离线数据缓存与网络恢复后的自动同步。

### ⚛️ 8. React Native 混合组件
- **AI 工作助手 (`AIWorkAssistantScreen`)**: 智能对话与任务辅助。
- **任务列表与报表 (`TaskListScreen`, `ReportsScreen`)**: 图表展示与实时任务追踪。
- **审批管理 (`ApprovalsScreen`)**: 流程节点查看与快速审批。

---

## 🌍 多语言国际化 (i18n)

应用内置完善的国际化支持：
- **简体中文 (`values/strings.xml`)**: 默认系统语言。
- **英文 (`values-en/strings.xml`)**: 完整英文文本支持。
- **切换机制**: 在登录页与系统设置页中支持一键切换语言，并通过 `SharedPreferences` 保存偏好，重新打开应用自动生效。

---

## 🛠️ 环境要求与构建指南

### 📋 环境要求
- **Android Studio**: Android Studio Ladybug (2024.2+) / Jellyfish 或更高版本
- **JDK**: Java 17 或 JDK 21 (推荐配置 `JAVA_HOME`)
- **Android SDK**: API Level 35 (Android 15)
- **Node.js**: v18.x 或更高版本
- **包管理器**: npm 或 yarn

### 🚀 构建与运行步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/tyler194653/SAAS.git
   cd SAAS/mobile-employee-simple
   ```

2. **安装 React Native 依赖**
   ```bash
   npm install
   ```

3. **启动 Metro 服务 (React Native 开发服务)**
   ```bash
   npm start
   ```

4. **编译与运行 Android 应用**
   - **方式一（Android Studio GUI）**: 
     1. 使用 Android Studio 打开 `mobile-employee-simple` 项目。
     2. 确保已关闭 Gradle 的 Offline Mode (离线模式)。
     3. 点击 **Sync Project with Gradle Files** 按钮。
     4. 选择连接的 Android 设备或模拟器，点击 **Run 'app'**。
   - **方式二（命令行）**:
     ```bash
     ./gradlew app:assembleDebug
     ```

---

## 📄 许可证

本项目采用 MIT 许可证，详情请参阅项目中的 [LICENSE](LICENSE) 文件。

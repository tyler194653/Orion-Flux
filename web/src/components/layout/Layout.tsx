import { useSelector, useDispatch } from 'react-redux'
import { RootState } from '../../store'
import { logout } from '../../store/slices/authSlice'
import { toggleSidebar } from '../../store/slices/uiSlice'

interface LayoutProps {
  children: React.ReactNode
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const dispatch = useDispatch()
  const { user } = useSelector((state: RootState) => state.auth)
  const { sidebarOpen } = useSelector((state: RootState) => state.ui)

  const handleLogout = () => {
    dispatch(logout())
  }

  return (
    <div className="flex min-h-screen bg-gray-50">
      {/* 左侧侧边栏 */}
      <div className={`bg-white shadow-lg transform transition-all duration-300 ease-in-out ${
        sidebarOpen ? 'w-64' : 'w-16'
      } ${
        sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
      } fixed lg:relative z-50 h-screen flex flex-col`}>
        
        {/* 侧边栏顶部标题区域 */}
        <div className="flex items-center justify-between h-16 px-4 bg-blue-600 flex-shrink-0">
          <div className="flex items-center justify-center flex-1">
            <h1 className={`font-bold text-white transition-all duration-300 ${
              sidebarOpen ? 'text-xl' : 'text-lg'
            }`}>
              {sidebarOpen ? '侧边栏' : '侧边栏'}
            </h1>
          </div>
          
          {/* 桌面端折叠按钮 */}
          <button
            onClick={() => dispatch(toggleSidebar())}
            className="hidden lg:block collapse-button"
            title={sidebarOpen ? '收起侧边栏' : '展开侧边栏'}
          >
            <svg className="h-5 w-5 transform transition-transform duration-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              {sidebarOpen ? (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 19l-7-7 7-7m8 14l-7-7 7-7" />
              ) : (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 5l7 7-7 7M5 5l7 7-7 7" />
              )}
            </svg>
          </button>
        </div>

        {/* 导航菜单 */}
        <nav className="flex-1 px-2 py-4 space-y-2 overflow-y-auto sidebar-scroll">
          <a 
            href="/dashboard" 
            className="sidebar-menu-item group"
            title={!sidebarOpen ? '仪表盘' : ''}
          >
            <svg className="sidebar-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2z" />
            </svg>
            <span className={`sidebar-text ${
              sidebarOpen ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-2 lg:opacity-0 lg:-translate-x-2'
            }`}>
              仪表盘
            </span>
            {/* 折叠状态提示 */}
            {!sidebarOpen && (
              <div className="sidebar-tooltip">
                仪表盘
              </div>
            )}
          </a>
          
          <a 
            href="/products" 
            className="sidebar-menu-item group"
            title={!sidebarOpen ? '产品管理' : ''}
          >
            <svg className="sidebar-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
            </svg>
            <span className={`sidebar-text ${
              sidebarOpen ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-2 lg:opacity-0 lg:-translate-x-2'
            }`}>
              产品管理
            </span>
            {!sidebarOpen && (
              <div className="sidebar-tooltip">
                产品管理
              </div>
            )}
          </a>
          
          <a 
            href="/orders" 
            className="sidebar-menu-item group"
            title={!sidebarOpen ? '订单管理' : ''}
          >
            <svg className="sidebar-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
            </svg>
            <span className={`sidebar-text ${
              sidebarOpen ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-2 lg:opacity-0 lg:-translate-x-2'
            }`}>
              订单管理
            </span>
            {!sidebarOpen && (
              <div className="sidebar-tooltip">
                订单管理
              </div>
            )}
          </a>
          
          <a 
            href="/inventory" 
            className="sidebar-menu-item group"
            title={!sidebarOpen ? '库存管理' : ''}
          >
            <svg className="sidebar-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            <span className={`sidebar-text ${
              sidebarOpen ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-2 lg:opacity-0 lg:-translate-x-2'
            }`}>
              库存管理
            </span>
            {!sidebarOpen && (
              <div className="sidebar-tooltip">
                库存管理
              </div>
            )}
          </a>
          
          <a 
            href="/suppliers" 
            className="sidebar-menu-item group"
            title={!sidebarOpen ? '供应商管理' : ''}
          >
            <svg className="sidebar-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <span className={`sidebar-text ${
              sidebarOpen ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-2 lg:opacity-0 lg:-translate-x-2'
            }`}>
              供应商管理
            </span>
            {!sidebarOpen && (
              <div className="sidebar-tooltip">
                供应商管理
              </div>
            )}
          </a>
        </nav>
      </div>

      {/* 右侧主要区域 */}
      <div className={`flex-1 flex flex-col page-transition ${
        sidebarOpen ? 'ml-0 lg:ml-0' : 'ml-0 lg:ml-0'
      }`}>
        {/* 顶部用户信息栏 */}
        <header className="bg-white shadow-sm border-b border-gray-200 flex-shrink-0">
          <div className="flex items-center justify-between h-16 px-4">
            {/* 移动端折叠按钮 */}
            <button
              onClick={() => dispatch(toggleSidebar())}
              className="lg:hidden p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-blue-500"
            >
              <svg className="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            </button>

            {/* 左侧空白区域或面包屑 */}
            <div className="flex items-center">
              <h2 className="text-lg font-semibold text-gray-900">
                {/* 这里可以根据当前路由显示页面标题 */}
                仪表盘
              </h2>
            </div>

            {/* 右侧用户信息 */}
            <div className="flex items-center space-x-4">
              {/* 通知图标 */}
              <button className="p-2 text-gray-400 hover:text-gray-500 hover:bg-gray-100 rounded-full">
                <svg className="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-5 5-5-5h5V3h5v14z" />
                </svg>
              </button>
              
              {/* 用户信息 */}
              <div className="flex items-center space-x-3">
                <div className="text-right">
                  <p className="text-sm font-medium text-gray-900">{user?.name}</p>
                  <p className="text-xs text-gray-500">管理员</p>
                </div>
                <div className="w-10 h-10 bg-blue-500 rounded-full flex items-center justify-center">
                  <span className="text-sm font-medium text-white">{user?.name?.[0]}</span>
                </div>
                
                {/* 下拉菜单按钮 */}
                <div className="relative">
                  <button
                    onClick={handleLogout}
                    className="p-2 text-gray-400 hover:text-gray-500 hover:bg-gray-100 rounded-full transition-colors"
                    title="退出登录"
                  >
                    <svg className="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </header>

        {/* 主要内容区域 */}
        <main className="flex-1 overflow-auto">
          {children}
        </main>
      </div>

      {/* 移动端侧边栏遮罩 */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 z-40 bg-black bg-opacity-50 lg:hidden"
          onClick={() => dispatch(toggleSidebar())}
        />
      )}
    </div>
  )
}

export default Layout 
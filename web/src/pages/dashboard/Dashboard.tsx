import SalesChart from '../../components/charts/SalesChart'
import SupplierChart from '../../components/charts/SupplierChart'

const Dashboard: React.FC = () => {
  return (
    <div className="p-6 space-y-6">
      {/* 页面标题 */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">仪表盘</h1>
        <p className="text-gray-600">欢迎使用供应链管理系统 · 今日概览</p>
      </div>

      {/* 统计卡片区域 */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 bg-blue-500 rounded-lg">
              <svg className="h-6 w-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">总订单</p>
              <p className="text-2xl font-semibold text-gray-900">1,234</p>
              <p className="text-xs text-green-600">↗ 12% 较昨日</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 bg-green-500 rounded-lg">
              <svg className="h-6 w-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">总收入</p>
              <p className="text-2xl font-semibold text-gray-900">¥123,456</p>
              <p className="text-xs text-green-600">↗ 8% 较昨日</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 bg-yellow-500 rounded-lg">
              <svg className="h-6 w-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">库存产品</p>
              <p className="text-2xl font-semibold text-gray-900">567</p>
              <p className="text-xs text-red-600">↘ 3% 较昨日</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow">
          <div className="flex items-center">
            <div className="p-3 bg-purple-500 rounded-lg">
              <svg className="h-6 w-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">供应商</p>
              <p className="text-2xl font-semibold text-gray-900">89</p>
              <p className="text-xs text-green-600">↗ 2 新增</p>
            </div>
          </div>
        </div>
      </div>

      {/* 快速操作区域 */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">快速操作</h3>
        <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
          <button className="flex flex-col items-center p-4 bg-blue-50 hover:bg-blue-100 rounded-lg transition-colors">
            <svg className="h-8 w-8 text-blue-600 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            <span className="text-sm font-medium text-gray-700">新建订单</span>
          </button>
          <button className="flex flex-col items-center p-4 bg-green-50 hover:bg-green-100 rounded-lg transition-colors">
            <svg className="h-8 w-8 text-green-600 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
            </svg>
            <span className="text-sm font-medium text-gray-700">添加产品</span>
          </button>
          <button className="flex flex-col items-center p-4 bg-yellow-50 hover:bg-yellow-100 rounded-lg transition-colors">
            <svg className="h-8 w-8 text-yellow-600 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            <span className="text-sm font-medium text-gray-700">库存调整</span>
          </button>
          <button className="flex flex-col items-center p-4 bg-purple-50 hover:bg-purple-100 rounded-lg transition-colors">
            <svg className="h-8 w-8 text-purple-600 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <span className="text-sm font-medium text-gray-700">新增供应商</span>
          </button>
          <button className="flex flex-col items-center p-4 bg-indigo-50 hover:bg-indigo-100 rounded-lg transition-colors">
            <svg className="h-8 w-8 text-indigo-600 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
            <span className="text-sm font-medium text-gray-700">查看报表</span>
          </button>
          <button className="flex flex-col items-center p-4 bg-red-50 hover:bg-red-100 rounded-lg transition-colors">
            <svg className="h-8 w-8 text-red-600 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L4.314 16.5c-.77.833.192 2.5 1.732 2.5z" />
            </svg>
            <span className="text-sm font-medium text-gray-700">风险预警</span>
          </button>
        </div>
      </div>

      {/* 主要内容区域 */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* 最近订单 */}
        <div className="lg:col-span-2 bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900">最近订单</h2>
            <button className="text-sm text-blue-600 hover:text-blue-700">查看全部</button>
          </div>
          <div className="space-y-4">
            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                  <span className="text-sm font-medium text-blue-600">#001</span>
                </div>
                <div className="ml-3">
                  <p className="text-sm font-medium text-gray-900">订单 #ORD-001</p>
                  <p className="text-sm text-gray-500">产品A - 100件 · 客户：ABC公司</p>
                </div>
              </div>
              <div className="text-right">
                <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-800 rounded-full">
                  已完成
                </span>
                <p className="text-sm text-gray-500 mt-1">¥12,000</p>
              </div>
            </div>
            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <div className="w-10 h-10 bg-yellow-100 rounded-lg flex items-center justify-center">
                  <span className="text-sm font-medium text-yellow-600">#002</span>
                </div>
                <div className="ml-3">
                  <p className="text-sm font-medium text-gray-900">订单 #ORD-002</p>
                  <p className="text-sm text-gray-500">产品B - 50件 · 客户：XYZ集团</p>
                </div>
              </div>
              <div className="text-right">
                <span className="px-2 py-1 text-xs font-medium bg-yellow-100 text-yellow-800 rounded-full">
                  处理中
                </span>
                <p className="text-sm text-gray-500 mt-1">¥8,500</p>
              </div>
            </div>
            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                  <span className="text-sm font-medium text-blue-600">#003</span>
                </div>
                <div className="ml-3">
                  <p className="text-sm font-medium text-gray-900">订单 #ORD-003</p>
                  <p className="text-sm text-gray-500">产品C - 200件 · 客户：德芙制造</p>
                </div>
              </div>
              <div className="text-right">
                <span className="px-2 py-1 text-xs font-medium bg-blue-100 text-blue-800 rounded-full">
                  待发货
                </span>
                <p className="text-sm text-gray-500 mt-1">¥15,600</p>
              </div>
            </div>
            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
                  <span className="text-sm font-medium text-purple-600">#004</span>
                </div>
                <div className="ml-3">
                  <p className="text-sm font-medium text-gray-900">订单 #ORD-004</p>
                  <p className="text-sm text-gray-500">产品D - 75件 · 客户：科技有限</p>
                </div>
              </div>
              <div className="text-right">
                <span className="px-2 py-1 text-xs font-medium bg-gray-100 text-gray-800 rounded-full">
                  待确认
                </span>
                <p className="text-sm text-gray-500 mt-1">¥9,800</p>
              </div>
            </div>
          </div>
        </div>

        {/* 右侧信息栏 */}
        <div className="space-y-6">
          {/* 库存警告 */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold text-gray-900">库存警告</h2>
              <span className="bg-red-100 text-red-800 text-xs font-medium px-2 py-1 rounded-full">3项</span>
            </div>
            <div className="space-y-3">
              <div className="flex items-center justify-between p-3 bg-red-50 rounded-lg">
                <div>
                  <p className="text-sm font-medium text-gray-900">产品X</p>
                  <p className="text-xs text-gray-500">当前库存: 5件</p>
                </div>
                <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-800 rounded-full">
                  库存不足
                </span>
              </div>
              <div className="flex items-center justify-between p-3 bg-yellow-50 rounded-lg">
                <div>
                  <p className="text-sm font-medium text-gray-900">产品Y</p>
                  <p className="text-xs text-gray-500">当前库存: 15件</p>
                </div>
                <span className="px-2 py-1 text-xs font-medium bg-yellow-100 text-yellow-800 rounded-full">
                  库存偏低
                </span>
              </div>
              <div className="flex items-center justify-between p-3 bg-orange-50 rounded-lg">
                <div>
                  <p className="text-sm font-medium text-gray-900">产品Z</p>
                  <p className="text-xs text-gray-500">当前库存: 8件</p>
                </div>
                <span className="px-2 py-1 text-xs font-medium bg-orange-100 text-orange-800 rounded-full">
                  需要补货
                </span>
              </div>
            </div>
          </div>

          {/* 系统通知 */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">系统通知</h3>
            <div className="space-y-3">
              <div className="flex items-start space-x-3">
                <div className="w-2 h-2 bg-blue-500 rounded-full mt-2"></div>
                <div>
                  <p className="text-sm text-gray-900">系统维护通知</p>
                  <p className="text-xs text-gray-500">今晚22:00-23:00进行例行维护</p>
                  <p className="text-xs text-gray-400">2小时前</p>
                </div>
              </div>
              <div className="flex items-start space-x-3">
                <div className="w-2 h-2 bg-green-500 rounded-full mt-2"></div>
                <div>
                  <p className="text-sm text-gray-900">新功能上线</p>
                  <p className="text-xs text-gray-500">AI智能分析功能现已可用</p>
                  <p className="text-xs text-gray-400">5小时前</p>
                </div>
              </div>
              <div className="flex items-start space-x-3">
                <div className="w-2 h-2 bg-yellow-500 rounded-full mt-2"></div>
                <div>
                  <p className="text-sm text-gray-900">订单提醒</p>
                  <p className="text-xs text-gray-500">5个订单待确认处理</p>
                  <p className="text-xs text-gray-400">1天前</p>
                </div>
              </div>
            </div>
          </div>

          {/* 今日任务 */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">今日任务</h3>
            <div className="space-y-3">
              <div className="flex items-center space-x-3">
                <input type="checkbox" className="h-4 w-4 text-blue-600 rounded" checked />
                <span className="text-sm text-gray-500 line-through">检查库存预警</span>
              </div>
              <div className="flex items-center space-x-3">
                <input type="checkbox" className="h-4 w-4 text-blue-600 rounded" />
                <span className="text-sm text-gray-900">处理待审核订单</span>
              </div>
              <div className="flex items-center space-x-3">
                <input type="checkbox" className="h-4 w-4 text-blue-600 rounded" />
                <span className="text-sm text-gray-900">联系供应商补货</span>
              </div>
              <div className="flex items-center space-x-3">
                <input type="checkbox" className="h-4 w-4 text-blue-600 rounded" />
                <span className="text-sm text-gray-900">更新产品价格</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 底部图表区域 */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* 销售趋势图表 */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">销售趋势</h3>
          <div className="h-64">
            <SalesChart />
          </div>
        </div>

        {/* 供应商分析图表 */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">供应商分析</h3>
          <div className="h-64">
            <SupplierChart />
          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard 
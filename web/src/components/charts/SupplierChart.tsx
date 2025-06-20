import { Doughnut } from 'react-chartjs-2'
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  ChartOptions
} from 'chart.js'

ChartJS.register(ArcElement, Tooltip, Legend)

const SupplierChart: React.FC = () => {
  const data = {
    labels: ['电子产品', '机械设备', '原材料', '包装材料', '其他'],
    datasets: [
      {
        data: [30, 25, 20, 15, 10],
        backgroundColor: [
          '#3B82F6', // 蓝色
          '#10B981', // 绿色
          '#F59E0B', // 黄色
          '#EF4444', // 红色
          '#8B5CF6', // 紫色
        ],
        borderColor: [
          '#2563EB',
          '#059669',
          '#D97706',
          '#DC2626',
          '#7C3AED',
        ],
        borderWidth: 2,
      },
    ],
  }

  const options: ChartOptions<'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 20,
          usePointStyle: true,
        },
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            const label = context.label || ''
            const value = context.parsed
            const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0)
            const percentage = ((value / total) * 100).toFixed(1)
            return `${label}: ${percentage}%`
          }
        }
      }
    },
    cutout: '60%',
  }

  return <Doughnut data={data} options={options} />
}

export default SupplierChart 
import React, { useState, useEffect } from 'react';
import {
  View,
  ScrollView,
  RefreshControl,
  StyleSheet,
  SafeAreaView,
  Dimensions,
} from 'react-native';
import {
  Box,
  Text,
  VStack,
  HStack,
  Button,
  Icon,
  Progress,
  Badge,
  Pressable,
  useToast
} from 'native-base';
import { MaterialIcons } from '@expo/vector-icons';
import { LineChart, BarChart } from 'react-native-chart-kit';

const { width } = Dimensions.get('window');

interface KPI {
  label: string;
  value: string;
  change: number;
  color: string;
  icon: string;
}

interface Alert {
  id: string;
  title: string;
  message: string;
  level: 'high' | 'medium' | 'low';
  timestamp: Date;
}

interface ManagerCockpitScreenProps {
  navigation: any;
}

const ManagerCockpitScreen: React.FC<ManagerCockpitScreenProps> = ({ navigation }) => {
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [kpis, setKpis] = useState<KPI[]>([]);
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [realtimeData, setRealtimeData] = useState({
    activeEmployees: 45,
    completionRate: 87,
    efficiency: 92,
    qualityScore: 94
  });
  const toast = useToast();

  useEffect(() => {
    loadDashboardData();
    // 模拟实时数据更新
    const interval = setInterval(updateRealtimeData, 30000); // 30秒更新一次
    return () => clearInterval(interval);
  }, []);

  const loadDashboardData = async () => {
    // 模拟加载KPI数据
    const mockKPIs: KPI[] = [
      {
        label: '日订单量',
        value: '1,234',
        change: 12.5,
        color: 'blue.500',
        icon: 'shopping-cart'
      },
      {
        label: '营业额',
        value: '¥567K',
        change: 8.3,
        color: 'green.500',
        icon: 'monetization-on'
      },
      {
        label: '库存周转',
        value: '2.4x',
        change: -2.1,
        color: 'orange.500',
        icon: 'inventory'
      },
      {
        label: '客户满意度',
        value: '94%',
        change: 3.7,
        color: 'purple.500',
        icon: 'star'
      }
    ];

    const mockAlerts: Alert[] = [
      {
        id: '1',
        title: '库存预警',
        message: '产品A库存低于安全库存线',
        level: 'high',
        timestamp: new Date()
      },
      {
        id: '2',
        title: '订单延迟',
        message: '3个订单超过预期交付时间',
        level: 'medium',
        timestamp: new Date(Date.now() - 30 * 60 * 1000)
      },
      {
        id: '3',
        title: '绩效异常',
        message: '仓库B区效率下降15%',
        level: 'medium',
        timestamp: new Date(Date.now() - 60 * 60 * 1000)
      }
    ];

    setKpis(mockKPIs);
    setAlerts(mockAlerts);
  };

  const updateRealtimeData = () => {
    setRealtimeData(prev => ({
      activeEmployees: prev.activeEmployees + Math.floor(Math.random() * 6) - 3,
      completionRate: Math.max(80, Math.min(95, prev.completionRate + Math.floor(Math.random() * 6) - 3)),
      efficiency: Math.max(85, Math.min(98, prev.efficiency + Math.floor(Math.random() * 4) - 2)),
      qualityScore: Math.max(90, Math.min(99, prev.qualityScore + Math.floor(Math.random() * 4) - 2))
    }));
  };

  const onRefresh = async () => {
    setIsRefreshing(true);
    await loadDashboardData();
    updateRealtimeData();
    setIsRefreshing(false);
  };

  const getAlertColor = (level: string) => {
    switch (level) {
      case 'high': return 'red.500';
      case 'medium': return 'orange.500';
      case 'low': return 'yellow.500';
      default: return 'gray.500';
    }
  };

  const formatTime = (date: Date): string => {
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    const minutes = Math.floor(diff / 60000);
    
    if (minutes < 1) return '刚刚';
    if (minutes < 60) return `${minutes}分钟前`;
    const hours = Math.floor(minutes / 60);
    if (hours < 24) return `${hours}小时前`;
    return date.toLocaleDateString();
  };

  // 图表数据
  const chartData = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
    datasets: [{
      data: [20, 45, 28, 80, 99, 43],
      strokeWidth: 2
    }]
  };

  const chartConfig = {
    backgroundGradientFrom: '#ffffff',
    backgroundGradientTo: '#ffffff',
    color: (opacity = 1) => `rgba(59, 130, 246, ${opacity})`,
    strokeWidth: 2,
    barPercentage: 0.5,
    useShadowColorFromDataset: false
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView
        refreshControl={
          <RefreshControl refreshing={isRefreshing} onRefresh={onRefresh} />
        }
      >
        {/* 顶部标题栏 */}
        <Box bg="blue.600" px={4} py={6}>
          <VStack space={3}>
            <HStack justifyContent="space-between" alignItems="center">
              <VStack>
                <Text color="white" fontSize="xl" fontWeight="bold">
                  管理驾驶舱
                </Text>
                <Text color="blue.100" fontSize="sm">
                  实时业务监控中心
                </Text>
              </VStack>
              <HStack space={2}>
                <Pressable onPress={() => navigation.navigate('AIAnalytics')}>
                  <Box bg="blue.500" rounded="full" p={2}>
                    <Icon as={MaterialIcons} name="auto-graph" size={5} color="white" />
                  </Box>
                </Pressable>
                <Pressable onPress={() => navigation.navigate('Settings')}>
                  <Box bg="blue.500" rounded="full" p={2}>
                    <Icon as={MaterialIcons} name="settings" size={5} color="white" />
                  </Box>
                </Pressable>
              </HStack>
            </HStack>
          </VStack>
        </Box>

        {/* 实时数据卡片 */}
        <Box px={4} py={4}>
          <VStack space={4}>
            <Text fontSize="lg" fontWeight="bold" color="gray.800">
              实时监控
            </Text>
            <HStack space={2} justifyContent="space-between">
              <Box flex={1} bg="white" rounded="lg" p={3} shadow={2}>
                <VStack space={1} alignItems="center">
                  <Icon as={MaterialIcons} name="people" size={6} color="blue.500" />
                  <Text fontSize="lg" fontWeight="bold" color="blue.500">
                    {realtimeData.activeEmployees}
                  </Text>
                  <Text fontSize="xs" color="gray.600" textAlign="center">
                    在线员工
                  </Text>
                </VStack>
              </Box>
              <Box flex={1} bg="white" rounded="lg" p={3} shadow={2}>
                <VStack space={1} alignItems="center">
                  <Icon as={MaterialIcons} name="task-alt" size={6} color="green.500" />
                  <Text fontSize="lg" fontWeight="bold" color="green.500">
                    {realtimeData.completionRate}%
                  </Text>
                  <Text fontSize="xs" color="gray.600" textAlign="center">
                    完成率
                  </Text>
                </VStack>
              </Box>
              <Box flex={1} bg="white" rounded="lg" p={3} shadow={2}>
                <VStack space={1} alignItems="center">
                  <Icon as={MaterialIcons} name="speed" size={6} color="orange.500" />
                  <Text fontSize="lg" fontWeight="bold" color="orange.500">
                    {realtimeData.efficiency}%
                  </Text>
                  <Text fontSize="xs" color="gray.600" textAlign="center">
                    效率指数
                  </Text>
                </VStack>
              </Box>
              <Box flex={1} bg="white" rounded="lg" p={3} shadow={2}>
                <VStack space={1} alignItems="center">
                  <Icon as={MaterialIcons} name="verified" size={6} color="purple.500" />
                  <Text fontSize="lg" fontWeight="bold" color="purple.500">
                    {realtimeData.qualityScore}%
                  </Text>
                  <Text fontSize="xs" color="gray.600" textAlign="center">
                    质量分数
                  </Text>
                </VStack>
              </Box>
            </HStack>
          </VStack>
        </Box>

        {/* 关键指标KPI */}
        <Box px={4} py={2}>
          <VStack space={4}>
            <HStack justifyContent="space-between" alignItems="center">
              <Text fontSize="lg" fontWeight="bold" color="gray.800">
                关键指标
              </Text>
              <Button
                variant="ghost"
                size="sm"
                onPress={() => navigation.navigate('DetailedAnalytics')}
              >
                详细分析
              </Button>
            </HStack>
            
            <VStack space={3}>
              {kpis.map((kpi, index) => (
                <Box key={index} bg="white" rounded="lg" p={4} shadow={2}>
                  <HStack justifyContent="space-between" alignItems="center">
                    <HStack space={3} alignItems="center">
                      <Box bg={`${kpi.color.split('.')[0]}.100`} rounded="full" p={2}>
                        <Icon
                          as={MaterialIcons}
                          name={kpi.icon}
                          size={5}
                          color={kpi.color}
                        />
                      </Box>
                      <VStack>
                        <Text fontSize="sm" color="gray.600">
                          {kpi.label}
                        </Text>
                        <Text fontSize="xl" fontWeight="bold" color="gray.800">
                          {kpi.value}
                        </Text>
                      </VStack>
                    </HStack>
                    <VStack alignItems="flex-end">
                      <HStack alignItems="center" space={1}>
                        <Icon
                          as={MaterialIcons}
                          name={kpi.change > 0 ? 'trending-up' : 'trending-down'}
                          size={4}
                          color={kpi.change > 0 ? 'green.500' : 'red.500'}
                        />
                        <Text
                          fontSize="sm"
                          color={kpi.change > 0 ? 'green.500' : 'red.500'}
                          fontWeight="medium"
                        >
                          {Math.abs(kpi.change)}%
                        </Text>
                      </HStack>
                      <Text fontSize="xs" color="gray.500">
                        vs 昨日
                      </Text>
                    </VStack>
                  </HStack>
                </Box>
              ))}
            </VStack>
          </VStack>
        </Box>

        {/* 趋势图表 */}
        <Box px={4} py={2}>
          <VStack space={4}>
            <Text fontSize="lg" fontWeight="bold" color="gray.800">
              业务趋势
            </Text>
            <Box bg="white" rounded="lg" p={4} shadow={2}>
              <Text fontSize="md" fontWeight="medium" color="gray.700" mb={3}>
                本周订单趋势
              </Text>
              <LineChart
                data={chartData}
                width={width - 64}
                height={200}
                chartConfig={chartConfig}
                bezier
                style={{
                  marginVertical: 8,
                  borderRadius: 8
                }}
              />
            </Box>
          </VStack>
        </Box>

        {/* 异常预警 */}
        <Box px={4} py={2}>
          <VStack space={4}>
            <HStack justifyContent="space-between" alignItems="center">
              <Text fontSize="lg" fontWeight="bold" color="gray.800">
                异常预警
              </Text>
              <Badge colorScheme="red" variant="subtle">
                {alerts.filter(a => a.level === 'high').length} 高优先级
              </Badge>
            </HStack>
            
            <VStack space={3}>
              {alerts.slice(0, 3).map((alert) => (
                <Pressable
                  key={alert.id}
                  onPress={() => navigation.navigate('AlertDetail', { alertId: alert.id })}
                >
                  <Box bg="white" rounded="lg" p={4} shadow={2}>
                    <HStack space={3} alignItems="flex-start">
                      <Box
                        bg={`${getAlertColor(alert.level).split('.')[0]}.100`}
                        rounded="full"
                        p={2}
                      >
                        <Icon
                          as={MaterialIcons}
                          name="warning"
                          size={4}
                          color={getAlertColor(alert.level)}
                        />
                      </Box>
                      <VStack flex={1} space={1}>
                        <HStack justifyContent="space-between" alignItems="flex-start">
                          <Text fontSize="md" fontWeight="bold" color="gray.800" flex={1}>
                            {alert.title}
                          </Text>
                          <Badge
                            colorScheme={alert.level === 'high' ? 'red' : alert.level === 'medium' ? 'orange' : 'yellow'}
                            variant="subtle"
                            size="sm"
                          >
                            {alert.level === 'high' ? '高' : alert.level === 'medium' ? '中' : '低'}
                          </Badge>
                        </HStack>
                        <Text fontSize="sm" color="gray.600">
                          {alert.message}
                        </Text>
                        <Text fontSize="xs" color="gray.500">
                          {formatTime(alert.timestamp)}
                        </Text>
                      </VStack>
                    </HStack>
                  </Box>
                </Pressable>
              ))}
            </VStack>
            
            {alerts.length > 3 && (
              <Button
                variant="outline"
                onPress={() => navigation.navigate('AllAlerts')}
              >
                查看全部预警 ({alerts.length})
              </Button>
            )}
          </VStack>
        </Box>

        {/* 快速操作 */}
        <Box px={4} py={2}>
          <VStack space={4}>
            <Text fontSize="lg" fontWeight="bold" color="gray.800">
              快速操作
            </Text>
            <HStack space={3} justifyContent="space-between">
              <Pressable
                style={styles.quickAction}
                onPress={() => navigation.navigate('TeamManagement')}
              >
                <Box bg="blue.100" rounded="full" p={3} mb={2}>
                  <Icon as={MaterialIcons} name="groups" size={6} color="blue.500" />
                </Box>
                <Text fontSize="xs" textAlign="center">
                  团队管理
                </Text>
              </Pressable>

              <Pressable
                style={styles.quickAction}
                onPress={() => navigation.navigate('ApprovalCenter')}
              >
                <Box bg="green.100" rounded="full" p={3} mb={2}>
                  <Icon as={MaterialIcons} name="approval" size={6} color="green.500" />
                </Box>
                <Text fontSize="xs" textAlign="center">
                  审批中心
                </Text>
              </Pressable>

              <Pressable
                style={styles.quickAction}
                onPress={() => navigation.navigate('Reports')}
              >
                <Box bg="orange.100" rounded="full" p={3} mb={2}>
                  <Icon as={MaterialIcons} name="analytics" size={6} color="orange.500" />
                </Box>
                <Text fontSize="xs" textAlign="center">
                  报表分析
                </Text>
              </Pressable>

              <Pressable
                style={styles.quickAction}
                onPress={() => navigation.navigate('AIInsights')}
              >
                <Box bg="purple.100" rounded="full" p={3} mb={2}>
                  <Icon as={MaterialIcons} name="psychology" size={6} color="purple.500" />
                </Box>
                <Text fontSize="xs" textAlign="center">
                  AI洞察
                </Text>
              </Pressable>
            </HStack>
          </VStack>
        </Box>

        {/* 底部间距 */}
        <Box h={4} />
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F3F4F6',
  },
  quickAction: {
    width: (width - 32 - 9) / 4,
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
  },
});

export default ManagerCockpitScreen; 
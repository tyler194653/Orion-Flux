import React from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';

const ReportsScreen: React.FC = () => {
  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>报表</Text>
        <Text style={styles.subtitle}>React Native 实现</Text>
      </View>
      
      <View style={styles.content}>
        <View style={styles.reportCard}>
          <Text style={styles.reportTitle}>今日概览</Text>
          <View style={styles.statsContainer}>
            <View style={styles.statItem}>
              <Text style={styles.statNumber}>25</Text>
              <Text style={styles.statLabel}>完成任务</Text>
            </View>
            <View style={styles.statItem}>
              <Text style={styles.statNumber}>8</Text>
              <Text style={styles.statLabel}>进行中</Text>
            </View>
            <View style={styles.statItem}>
              <Text style={styles.statNumber}>3</Text>
              <Text style={styles.statLabel}>待处理</Text>
            </View>
          </View>
        </View>

        <View style={styles.reportCard}>
          <Text style={styles.reportTitle}>库存状态</Text>
          <View style={styles.inventoryItem}>
            <Text style={styles.inventoryName}>产品A</Text>
            <Text style={styles.inventoryQuantity}>100 个</Text>
            <Text style={styles.inventoryStatus}>正常</Text>
          </View>
          <View style={styles.inventoryItem}>
            <Text style={styles.inventoryName}>产品B</Text>
            <Text style={styles.inventoryQuantity}>50 箱</Text>
            <Text style={styles.inventoryStatus}>低库存</Text>
          </View>
          <View style={styles.inventoryItem}>
            <Text style={styles.inventoryName}>产品C</Text>
            <Text style={styles.inventoryQuantity}>200 件</Text>
            <Text style={styles.inventoryStatus}>正常</Text>
          </View>
        </View>

        <View style={styles.reportCard}>
          <Text style={styles.reportTitle}>质量报告</Text>
          <View style={styles.qualityItem}>
            <Text style={styles.qualityMetric}>合格率</Text>
            <Text style={styles.qualityValue}>98.5%</Text>
          </View>
          <View style={styles.qualityItem}>
            <Text style={styles.qualityMetric}>返工率</Text>
            <Text style={styles.qualityValue}>1.2%</Text>
          </View>
          <View style={styles.qualityItem}>
            <Text style={styles.qualityMetric}>客户满意度</Text>
            <Text style={styles.qualityValue}>4.8/5.0</Text>
          </View>
        </View>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    padding: 20,
    backgroundColor: '#9C27B0',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 4,
  },
  subtitle: {
    fontSize: 14,
    color: 'rgba(255, 255, 255, 0.8)',
  },
  content: {
    padding: 16,
  },
  reportCard: {
    backgroundColor: 'white',
    padding: 16,
    marginBottom: 16,
    borderRadius: 8,
    elevation: 2,
  },
  reportTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
    color: '#333',
  },
  statsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
  },
  statItem: {
    alignItems: 'center',
  },
  statNumber: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#9C27B0',
    marginBottom: 4,
  },
  statLabel: {
    fontSize: 14,
    color: '#666',
  },
  inventoryItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  inventoryName: {
    fontSize: 16,
    fontWeight: '500',
    flex: 1,
  },
  inventoryQuantity: {
    fontSize: 14,
    color: '#666',
    marginRight: 16,
  },
  inventoryStatus: {
    fontSize: 14,
    color: '#4CAF50',
    fontWeight: '500',
  },
  qualityItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  qualityMetric: {
    fontSize: 16,
    color: '#333',
  },
  qualityValue: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#9C27B0',
  },
});

export default ReportsScreen; 
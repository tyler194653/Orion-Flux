import React from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';

const WorkbenchScreen: React.FC = () => {
  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>工作台</Text>
        <Text style={styles.subtitle}>React Native 实现</Text>
      </View>
      
      <View style={styles.content}>
        <Text style={styles.sectionTitle}>今日任务</Text>
        <View style={styles.taskItem}>
          <Text style={styles.taskTitle}>库存盘点</Text>
          <Text style={styles.taskStatus}>进行中</Text>
        </View>
        <View style={styles.taskItem}>
          <Text style={styles.taskTitle}>订单处理</Text>
          <Text style={styles.taskStatus}>待处理</Text>
        </View>
        <View style={styles.taskItem}>
          <Text style={styles.taskTitle}>质量检查</Text>
          <Text style={styles.taskStatus}>已完成</Text>
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
    backgroundColor: '#2196F3',
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
    padding: 20,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  taskItem: {
    backgroundColor: 'white',
    padding: 16,
    marginBottom: 12,
    borderRadius: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  taskTitle: {
    fontSize: 16,
    fontWeight: '500',
  },
  taskStatus: {
    fontSize: 14,
    color: '#2196F3',
    fontWeight: '500',
  },
});

export default WorkbenchScreen; 
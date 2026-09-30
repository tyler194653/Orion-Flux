import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import WorkbenchScreen from '../screens/WorkbenchScreen';
import AIWorkAssistantScreen from '../screens/AIWorkAssistantScreen';
import { ApprovalsScreen } from '../screens/ApprovalsScreen';
import { ProfileScreen } from '../screens/ProfileScreen';

const Tab = createBottomTabNavigator();

export const BottomTabNavigator = () => {
  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
      }}
    >
      <Tab.Screen name="Workbench" component={WorkbenchScreen} />
      <Tab.Screen name="AI Chat" component={AIWorkAssistantScreen} />
      <Tab.Screen name="Approvals" component={ApprovalsScreen} />
      <Tab.Screen name="Profile" component={ProfileScreen} />
    </Tab.Navigator>
  );
};

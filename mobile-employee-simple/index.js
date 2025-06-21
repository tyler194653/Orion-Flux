import { AppRegistry } from 'react-native';
import App from './src/App';

// 注册主应用
AppRegistry.registerComponent('App', () => App);

// 注册各个屏幕组件
import WorkbenchScreen from './src/screens/WorkbenchScreen';
import AIWorkAssistantScreen from './src/screens/AIWorkAssistantScreen';
import TaskListScreen from './src/screens/TaskListScreen';
import ReportsScreen from './src/screens/ReportsScreen';

AppRegistry.registerComponent('WorkbenchScreen', () => WorkbenchScreen);
AppRegistry.registerComponent('AIWorkAssistantScreen', () => AIWorkAssistantScreen);
AppRegistry.registerComponent('TaskListScreen', () => TaskListScreen);
AppRegistry.registerComponent('ReportsScreen', () => ReportsScreen); 
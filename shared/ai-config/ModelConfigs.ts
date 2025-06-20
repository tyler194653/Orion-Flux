export interface ModelConfig {
  id: string;
  name: string;
  provider: 'siliconflow' | 'openai' | 'deepseek';
  type: 'language' | 'vision' | 'speech' | 'image' | 'reasoning';
  maxTokens: number;
  costPer1kTokens: number;
  isFree: boolean;
  description: string;
  capabilities: string[];
}

// SiliconFlow 支持的模型配置
export const SILICONFLOW_MODELS: ModelConfig[] = [
  // 语言模型
  {
    id: 'Qwen/Qwen2.5-72B-Instruct',
    name: 'Qwen2.5-72B',
    provider: 'siliconflow',
    type: 'language',
    maxTokens: 32768,
    costPer1kTokens: 0.003,
    isFree: false,
    description: '强大的大型语言模型，适合复杂任务',
    capabilities: ['text-generation', 'conversation', 'reasoning', 'coding']
  },
  {
    id: 'Qwen/Qwen2.5-7B-Instruct',
    name: 'Qwen2.5-7B',
    provider: 'siliconflow',
    type: 'language',
    maxTokens: 32768,
    costPer1kTokens: 0,
    isFree: true,
    description: '免费的中型语言模型，适合一般任务',
    capabilities: ['text-generation', 'conversation', 'basic-reasoning']
  },
  {
    id: 'deepseek-ai/DeepSeek-V2.5',
    name: 'DeepSeek-V2.5',
    provider: 'siliconflow',
    type: 'language',
    maxTokens: 32768,
    costPer1kTokens: 0.002,
    isFree: false,
    description: '优秀的编程和推理模型',
    capabilities: ['text-generation', 'coding', 'reasoning', 'math']
  },
  {
    id: 'THUDM/glm-4-9b-chat',
    name: 'GLM-4-9B-Chat',
    provider: 'siliconflow',
    type: 'language',
    maxTokens: 8192,
    costPer1kTokens: 0,
    isFree: true,
    description: '智谱AI的对话模型',
    capabilities: ['conversation', 'text-generation', 'chinese-nlp']
  },
  
  // 推理模型
  {
    id: 'deepseek-ai/DeepSeek-R1',
    name: 'DeepSeek-R1',
    provider: 'siliconflow',
    type: 'reasoning',
    maxTokens: 32768,
    costPer1kTokens: 0.005,
    isFree: false,
    description: '专门优化的推理模型',
    capabilities: ['reasoning', 'problem-solving', 'logical-thinking']
  },
  
  // 代码模型
  {
    id: 'deepseek-ai/DeepSeek-Coder-V2-Instruct',
    name: 'DeepSeek-Coder-V2',
    provider: 'siliconflow',
    type: 'language',
    maxTokens: 32768,
    costPer1kTokens: 0.002,
    isFree: false,
    description: '专业的代码生成模型',
    capabilities: ['coding', 'code-review', 'debugging', 'refactoring']
  },
  
  // 语音模型
  {
    id: 'FunAudioLLM/SenseVoice-Small',
    name: 'SenseVoice-Small',
    provider: 'siliconflow',
    type: 'speech',
    maxTokens: 0,
    costPer1kTokens: 0.001,
    isFree: false,
    description: '语音识别和处理模型',
    capabilities: ['speech-recognition', 'audio-processing']
  }
];

// 根据使用场景推荐模型
export const MODEL_RECOMMENDATIONS = {
  // 供应链管理场景
  inventory_analysis: ['Qwen/Qwen2.5-72B-Instruct', 'deepseek-ai/DeepSeek-V2.5'],
  order_processing: ['Qwen/Qwen2.5-7B-Instruct', 'THUDM/glm-4-9b-chat'],
  customer_service: ['Qwen/Qwen2.5-7B-Instruct', 'THUDM/glm-4-9b-chat'],
  data_analysis: ['Qwen/Qwen2.5-72B-Instruct', 'deepseek-ai/DeepSeek-V2.5'],
  
  // 移动端场景
  mobile_assistant: ['Qwen/Qwen2.5-7B-Instruct', 'THUDM/glm-4-9b-chat'],
  voice_commands: ['FunAudioLLM/SenseVoice-Small'],
  quick_queries: ['Qwen/Qwen2.5-7B-Instruct'],
  
  // 管理决策场景
  strategic_planning: ['Qwen/Qwen2.5-72B-Instruct', 'deepseek-ai/DeepSeek-R1'],
  risk_assessment: ['deepseek-ai/DeepSeek-R1', 'Qwen/Qwen2.5-72B-Instruct'],
  performance_analysis: ['deepseek-ai/DeepSeek-V2.5', 'Qwen/Qwen2.5-72B-Instruct']
};

// 获取免费模型列表
export const getFreeModels = (): ModelConfig[] => {
  return SILICONFLOW_MODELS.filter(model => model.isFree);
};

// 根据能力查找模型
export const getModelsByCapability = (capability: string): ModelConfig[] => {
  return SILICONFLOW_MODELS.filter(model => 
    model.capabilities.includes(capability)
  );
};

// 获取推荐模型
export const getRecommendedModels = (scenario: keyof typeof MODEL_RECOMMENDATIONS): ModelConfig[] => {
  const modelIds = MODEL_RECOMMENDATIONS[scenario] || [];
  return SILICONFLOW_MODELS.filter(model => modelIds.includes(model.id));
}; 
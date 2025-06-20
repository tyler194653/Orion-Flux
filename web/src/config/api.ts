export const API_CONFIG = {
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:5000/api',
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT || '10000'),
  headers: {
    'Content-Type': 'application/json',
  },
}

export const SILICONFLOW_CONFIG = {
  apiKey: import.meta.env.VITE_SILICONFLOW_API_KEY || '',
  baseURL: import.meta.env.VITE_SILICONFLOW_BASE_URL || 'https://api.siliconflow.cn/v1',
  models: {
    chat: 'Qwen/Qwen2.5-72B-Instruct',
    reasoning: 'deepseek-ai/DeepSeek-R1-Distill-Qwen-32B',
    vision: 'OpenGVLab/InternVL2-26B',
    tts: 'FunAudioLLM/SenseVoice-Small',
    image: 'black-forest-labs/FLUX.1-schnell',
  },
}

export const APP_CONFIG = {
  name: import.meta.env.VITE_APP_NAME || '供应链管理SaaS系统',
  version: import.meta.env.VITE_APP_VERSION || '1.0.0',
  env: import.meta.env.VITE_APP_ENV || 'development',
  features: {
    ai: import.meta.env.VITE_ENABLE_AI_FEATURES === 'true',
    offline: import.meta.env.VITE_ENABLE_OFFLINE_MODE === 'true',
    debug: import.meta.env.VITE_ENABLE_DEBUG === 'true',
  },
} 
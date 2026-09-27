import OpenAI from 'openai';
import { ModelConfig, SILICONFLOW_MODELS } from '../ai-config/ModelConfigs';

export interface SiliconFlowConfig {
  apiKey: string;
  baseURL?: string;
  defaultModel?: string;
  timeout?: number;
  maxRetries?: number;
}

export interface ChatMessage {
  role: 'system' | 'user' | 'assistant';
  content: string;
}

export interface ChatResponse {
  content: string;
  model: string;
  usage: {
    prompt_tokens: number;
    completion_tokens: number;
    total_tokens: number;
  };
  finish_reason: string;
}

export class SiliconFlowService {
  private client: OpenAI;
  private config: SiliconFlowConfig;

  constructor(config: SiliconFlowConfig) {
    this.config = {
      baseURL: 'https://api.siliconflow.cn/v1',
      defaultModel: 'Qwen/Qwen2.5-7B-Instruct',
      timeout: 30000,
      maxRetries: 3,
      ...config
    };

    this.client = new OpenAI({
      apiKey: this.config.apiKey,
      baseURL: this.config.baseURL,
      timeout: this.config.timeout,
      maxRetries: this.config.maxRetries
    });
  }

  /**
   * 发送聊天消息
   */
  async chat(
    messages: ChatMessage[],
    options?: {
      model?: string;
      maxTokens?: number;
      temperature?: number;
      stream?: boolean;
    }
  ): Promise<ChatResponse> {
    try {
      const model = options?.model || this.config.defaultModel!;
      
      const response = await this.client.chat.completions.create({
        model,
        messages,
        max_tokens: options?.maxTokens || 2048,
        temperature: options?.temperature || 0.7,
        stream: options?.stream || false
      });

      const choice = response.choices[0];
      
      return {
        content: choice.message.content || '',
        model: response.model,
        usage: {
          prompt_tokens: response.usage?.prompt_tokens || 0,
          completion_tokens: response.usage?.completion_tokens || 0,
          total_tokens: response.usage?.total_tokens || 0
        },
        finish_reason: choice.finish_reason || 'stop'
      };
    } catch (error) {
      throw new Error(`聊天请求失败: ${error instanceof Error ? error.message : '未知错误'}`);
    }
  }

  /**
   * 供应链数据分析
   */
  async analyzeSupplyChainData(data: any, analysisType: string): Promise<string> {
    const systemPrompt = `你是一个专业的供应链数据分析师。请分析以下数据并提供专业的洞察和建议。
    分析类型: ${analysisType}
    
    请从以下角度进行分析：
    1. 数据趋势和模式
    2. 潜在风险和机会
    3. 改进建议
    4. 预测和展望`;

    const messages: ChatMessage[] = [
      { role: 'system', content: systemPrompt },
      { role: 'user', content: JSON.stringify(data) }
    ];

    const response = await this.chat(messages, {
      model: 'Qwen/Qwen2.5-72B-Instruct',
      maxTokens: 4000
    });

    return response.content;
  }

  /**
   * 库存优化建议
   */
  async getInventoryOptimization(inventoryData: any): Promise<string> {
    const systemPrompt = `你是一个库存管理专家。请分析库存数据并提供优化建议。
    
    请重点关注：
    1. 库存周转率分析
    2. 安全库存建议
    3. 滞销产品识别
    4. 补货策略优化
    5. 成本控制措施`;

    const messages: ChatMessage[] = [
      { role: 'system', content: systemPrompt },
      { role: 'user', content: `库存数据: ${JSON.stringify(inventoryData)}` }
    ];

    const response = await this.chat(messages, {
      model: 'deepseek-ai/DeepSeek-V2.5',
      maxTokens: 3000
    });

    return response.content;
  }

  /**
   * 智能客服助手
   */
  async customerServiceChat(userMessage: string, context?: any): Promise<string> {
    const systemPrompt = `你是一个专业的供应链管理系统客服助手。请用友好、专业的态度回答用户问题。
    
    你可以帮助用户：
    1. 订单查询和状态跟踪
    2. 产品信息咨询
    3. 库存状态查询
    4. 系统使用指导
    5. 问题解决建议
    
    请用中文回答，保持专业和友好的语气。`;

    const messages: ChatMessage[] = [
      { role: 'system', content: systemPrompt }
    ];

    if (context) {
      messages.push({
        role: 'system',
        content: `相关上下文信息: ${JSON.stringify(context)}`
      });
    }

    messages.push({ role: 'user', content: userMessage });

    const response = await this.chat(messages, {
      model: 'Qwen/Qwen2.5-7B-Instruct',
      maxTokens: 1500
    });

    return response.content;
  }

  /**
   * 生成业务报告
   */
  async generateBusinessReport(data: any, reportType: string): Promise<string> {
    const systemPrompt = `你是一个商业分析专家。请基于提供的数据生成专业的业务报告。
    
    报告类型: ${reportType}
    
    报告应包含：
    1. 执行摘要
    2. 关键指标分析
    3. 趋势分析
    4. 风险评估
    5. 行动建议
    
    请用专业的商业语言，结构清晰，内容详实。`;

    const messages: ChatMessage[] = [
      { role: 'system', content: systemPrompt },
      { role: 'user', content: JSON.stringify(data) }
    ];

    const response = await this.chat(messages, {
      model: 'Qwen/Qwen2.5-72B-Instruct',
      maxTokens: 5000
    });

    return response.content;
  }

  /**
   * 智能任务分配（移动端）
   */
  async suggestTaskAssignment(employeeData: any, tasks: any[]): Promise<string> {
    const systemPrompt = `你是一个智能任务分配助手。请根据员工技能、工作负荷和任务要求，提供最优的任务分配建议。
    
    考虑因素：
    1. 员工技能匹配度
    2. 当前工作负荷
    3. 任务优先级
    4. 地理位置因素
    5. 历史表现
    
    请提供具体的分配建议和理由。`;

    const messages: ChatMessage[] = [
      { role: 'system', content: systemPrompt },
      { 
        role: 'user', 
        content: `员工信息: ${JSON.stringify(employeeData)}\n任务列表: ${JSON.stringify(tasks)}` 
      }
    ];

    const response = await this.chat(messages, {
      model: 'deepseek-ai/DeepSeek-V2.5',
      maxTokens: 2500
    });

    return response.content;
  }

  /**
   * 获取可用模型列表
   */
  getAvailableModels(): ModelConfig[] {
    return SILICONFLOW_MODELS;
  }

  /**
   * 检查API密钥有效性
   */
  async validateApiKey(): Promise<boolean> {
    try {
      await this.chat([
        { role: 'user', content: '测试连接' }
      ], { 
        model: 'Qwen/Qwen2.5-7B-Instruct',
        maxTokens: 10 
      });
      return true;
    } catch (error) {
      return false;
    }
  }

  /**
   * 获取使用统计
   */
  async getUsageStats(): Promise<any> {
    // 这里可以实现使用统计的获取逻辑
    // 需要根据SiliconFlow的实际API来实现
    return {
      totalTokens: 0,
      totalCost: 0,
      requestCount: 0
    };
  }
} 
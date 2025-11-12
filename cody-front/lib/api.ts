/**
 * Cody Wardrobe API Client
 * Backend API 연동을 위한 클라이언트 라이브러리
 */

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

interface ApiResponse<T> {
  result: boolean;
  data: T;
  error?: string;
}

// ============================================
// Types
// ============================================

export interface Item {
  id: number;
  category: string;
  name: string;
  imageUrl: string;
  color: string;
  season: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface OutfitItem {
  id: number;
  item: Item;
}

export interface Outfit {
  id: number;
  name: string;
  rating: number;
  formalityLevel: number;
  lastWornDate: string | null;
  wornCount: number;
  outfitItems: OutfitItem[];
  createdAt: string;
  updatedAt: string;
}

export interface History {
  id: number;
  outfit: Outfit;
  wornDate: string;
  createdAt: string;
}

export interface CreateItemRequest {
  category: string;
  name: string;
  imageUrl: string;
  color: string;
  season?: string;
}

export interface CreateOutfitRequest {
  name: string;
  rating: number;
  formalityLevel: number;
  itemIds: number[];
}

export interface CreateHistoryRequest {
  outfitId: number;
  wornDate: string;
}

// ============================================
// API Client
// ============================================

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private async request<T>(
    endpoint: string,
    options?: RequestInit
  ): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;

    const response = await fetch(url, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
    });

    if (!response.ok) {
      throw new Error(`API Error: ${response.status} ${response.statusText}`);
    }

    const data: ApiResponse<T> = await response.json();

    if (!data.result) {
      throw new Error(data.error || 'API request failed');
    }

    return data.data;
  }

  // ============================================
  // Item APIs
  // ============================================

  async getItems(): Promise<Item[]> {
    return this.request<Item[]>('/items');
  }

  async getItem(id: number): Promise<Item> {
    return this.request<Item>(`/items/${id}`);
  }

  async createItem(data: CreateItemRequest): Promise<Item> {
    return this.request<Item>('/items', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async updateItem(id: number, data: Partial<CreateItemRequest>): Promise<Item> {
    return this.request<Item>(`/items/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async deleteItem(id: number): Promise<void> {
    return this.request<void>(`/items/${id}`, {
      method: 'DELETE',
    });
  }

  async getItemsByCategory(category: string): Promise<Item[]> {
    return this.request<Item[]>(`/items/category/${category}`);
  }

  async getItemsBySeason(season: string): Promise<Item[]> {
    return this.request<Item[]>(`/items/season/${season}`);
  }

  async getItemsByColor(color: string): Promise<Item[]> {
    return this.request<Item[]>(`/items/color/${color}`);
  }

  // ============================================
  // Outfit APIs
  // ============================================

  async getOutfits(): Promise<Outfit[]> {
    return this.request<Outfit[]>('/outfits');
  }

  async getOutfit(id: number): Promise<Outfit> {
    return this.request<Outfit>(`/outfits/${id}`);
  }

  async createOutfit(data: CreateOutfitRequest): Promise<Outfit> {
    return this.request<Outfit>('/outfits', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async updateOutfit(id: number, data: Partial<CreateOutfitRequest>): Promise<Outfit> {
    return this.request<Outfit>(`/outfits/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async deleteOutfit(id: number): Promise<void> {
    return this.request<void>(`/outfits/${id}`, {
      method: 'DELETE',
    });
  }

  async getOutfitsByRating(minRating: number): Promise<Outfit[]> {
    return this.request<Outfit[]>(`/outfits/rating/${minRating}`);
  }

  async getOutfitsByFormality(minFormality: number, maxFormality: number): Promise<Outfit[]> {
    return this.request<Outfit[]>(`/outfits/formality?min=${minFormality}&max=${maxFormality}`);
  }

  async addItemToOutfit(outfitId: number, itemId: number): Promise<Outfit> {
    return this.request<Outfit>(`/outfits/${outfitId}/items/${itemId}`, {
      method: 'POST',
    });
  }

  async removeItemFromOutfit(outfitId: number, itemId: number): Promise<Outfit> {
    return this.request<Outfit>(`/outfits/${outfitId}/items/${itemId}`, {
      method: 'DELETE',
    });
  }

  async recordOutfitWorn(outfitId: number, wornDate: string): Promise<void> {
    return this.request<void>(`/outfits/${outfitId}/worn?date=${wornDate}`, {
      method: 'POST',
    });
  }

  async recommendOutfits(params: {
    minRating?: number;
    minFormality?: number;
    excludeRecent?: boolean;
    excludeDays?: number;
  }): Promise<Outfit[]> {
    const queryParams = new URLSearchParams();
    if (params.minRating !== undefined) queryParams.append('minRating', params.minRating.toString());
    if (params.minFormality !== undefined) queryParams.append('minFormality', params.minFormality.toString());
    if (params.excludeRecent !== undefined) queryParams.append('excludeRecent', params.excludeRecent.toString());
    if (params.excludeDays !== undefined) queryParams.append('excludeDays', params.excludeDays.toString());

    return this.request<Outfit[]>(`/outfits/recommend?${queryParams.toString()}`);
  }

  // ============================================
  // History APIs
  // ============================================

  async getHistories(): Promise<History[]> {
    return this.request<History[]>('/histories');
  }

  async getHistory(id: number): Promise<History> {
    return this.request<History>(`/histories/${id}`);
  }

  async createHistory(data: CreateHistoryRequest): Promise<History> {
    return this.request<History>('/histories', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async deleteHistory(id: number): Promise<void> {
    return this.request<void>(`/histories/${id}`, {
      method: 'DELETE',
    });
  }

  async getHistoriesByOutfit(outfitId: number): Promise<History[]> {
    return this.request<History[]>(`/histories/outfit/${outfitId}`);
  }

  async getHistoriesByDateRange(startDate: string, endDate: string): Promise<History[]> {
    return this.request<History[]>(`/histories/date-range?startDate=${startDate}&endDate=${endDate}`);
  }

  async getHistoriesByMonth(year: number, month: number): Promise<History[]> {
    return this.request<History[]>(`/histories/month?year=${year}&month=${month}`);
  }
}

// Export singleton instance
export const api = new ApiClient(API_BASE_URL);

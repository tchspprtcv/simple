import { TrackingResponse } from './types';

export async function getTrackingInfo(trackingCode: string): Promise<TrackingResponse> {
  try {
    const response = await fetch(`/api/tracking?code=${encodeURIComponent(trackingCode)}`);
    const data = await response.json();

    if (!response.ok) {
      return {
        success: false,
        error: data.error || 'Erro ao buscar informações do pedido'
      };
    }

    return data;
  } catch (error) {
    console.error('Error in tracking service:', error);
    return {
      success: false,
      error: 'Erro ao buscar informações do pedido. Tente novamente mais tarde.'
    };
  }
}
export interface RequestStatus {
  id: string;
  trackingCode: string;
  currentStatus: string;
  history: StatusHistory[];
}

export interface StatusHistory {
  date: string;
  status: string;
  description: string;
}

export interface TrackingResponse {
  success: boolean;
  data?: RequestStatus;
  error?: string;
}
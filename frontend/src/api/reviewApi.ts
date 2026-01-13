import { http } from './http';

export type ReviewResponse = {
  id: number;
  doctorId: number;
  patientId: number;
  patientName: string;
  rating: number;
  comment: string;
  createdAt: string;
};

export type CreateReviewRequest = {
  doctorId: number;
  rating: number; // 1..5
  comment: string;
};

export async function listReviewsByDoctor(doctorId: number): Promise<ReviewResponse[]> {
  const { data } = await http.get<ReviewResponse[]>(`/reviews/doctor/${doctorId}`);
  return data;
}

export async function createReview(req: CreateReviewRequest): Promise<ReviewResponse> {
  const { data } = await http.post<ReviewResponse>('/reviews', req);
  return data;
}

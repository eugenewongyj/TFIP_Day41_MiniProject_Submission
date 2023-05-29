export interface Review {
    id?: string;
    toiletName: string;
    rating: number;
    comment: string;
    email?: string;
    submittedDate?: Date;
}

// This is required because BE sends submittedDate as string
export interface ReviewDto {
    id: string;
    toiletName: string;
    rating: number;
    comment: string;
    email: string;
    submittedDate: string;
}
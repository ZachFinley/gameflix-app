export type Game = {
    id: number;
    title?: string;
    slug?: string;
    esrbRating?: string;
    releaseDate?: string;
    status?: string;
    description?: string;
    boxArtUrl?: string;
};

export type LibraryEntry = { 
    userId: number; 
    gameId: number; 
    addedAt?: string;
    source?: string 
};

export type User = { 
    id: number; 
    displayName: string; 
    email?: string;
    admin?: boolean 
};

export type WishlistEntry = { 
    userId: number; 
    gameId: number; 
    addedAt?: string 
};

export type Review = {
    id: number;
    userId: number;
    gameId: number;
    rating: number;
    comment?: string;
    createdAt?: string;
    updatedAt?: string;
};
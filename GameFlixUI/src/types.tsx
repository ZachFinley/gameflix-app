export type Game = {
    id: number;
    title?: string;
    slug?: string;
    esrbRating?: string;
    releaseDate?: string;
    status?: string;
    description?: string;
    imageUrl?: string;
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
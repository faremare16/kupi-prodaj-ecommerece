import { Category } from "./category";

export interface Product{
    id: number;
    sku: string;
    name: string;
    description: string;
    imageUrl: string;
    price: number;
    category: Category;
    unitsInStock: number;
    datePublished?: Date;
    dateUpdated?: Date;
}
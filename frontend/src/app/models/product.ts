import { Category } from "./category";
import { User } from "./user";

export interface Product{
    id: number;
    sku: string;
    name: string;
    description: string;
    imageUrl: string;
    price: number;
    category: Category;
    user: User;
    unitsInStock: number;
    datePublished?: Date;
    dateUpdated?: Date;
}
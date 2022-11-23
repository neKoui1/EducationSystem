export interface Student {
	id: number;
	name: string;
	password: string;
	age: number;
	sex: string;
	dept: string;
	course: [] | null;
}
export interface Course {
	id: number;
	name: string;
	credit: number;
	taught: string | null;
	capacity: number;
	choose: number;
	location: string;
	time: string;
}

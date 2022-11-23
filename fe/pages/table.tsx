import { Table } from '@arco-design/web-react';
import useSWR from 'swr';
import Side from '../components/Side';
import { Course } from '../type';
const columns: any[] = [
	{
		title: 'Name',
		dataIndex: 'name',
	},
	{
		title: '学分',
		dataIndex: 'credit',
	},
	{
		title: '任课教师',
		dataIndex: 'taught',
	},
];
const App = () => {
	const { data, error } = useSWR<Course[]>('/api/course/list', (url) =>
		fetch(url).then((r) => r.json())
	);
	if (error) return <div>failed to load</div>;
	if (!data) return <div>loading...</div>;
	else console.log(data);
	return (
		<Side>
			<Table columns={columns} data={data} />
		</Side>
	);
};

export default App;

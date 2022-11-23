import { Button, Grid, Input, Message } from '@arco-design/web-react';
import '@arco-design/web-react/dist/css/arco.css';
import Router from 'next/router';
import { useEffect, useState } from 'react';
import Student from '../../components/StuSide';
import TableContainer from '../../components/TableContainer';
import { Course, Student as Stu } from '../../type';
const Row = Grid.Row;
const Col = Grid.Col;
const InputSearch = Input.Search;
const handleChoose = (id: number, stuId: number) => {
	fetch(`/api/student/chooseCourse?course_id=${id}&student_id=${stuId}`, {
		method: 'POST',
	})
		.then((r) => r.json())
		.then((r) => {
			if (r.code == 200) {
				Message.success('选课成功');
			} else {
				Message.error(r.msg);
			}
		});
};
const Courses = () => {
	const [stu, setStu] = useState<Stu>({} as Stu);
	const [url, setUrl] = useState<string>('');
	useEffect(() => {
		let stu = localStorage.getItem('stu');
		if (stu) {
			setStu(JSON.parse(stu));
		} else {
			Router.push('/');
		}
	}, []);
	const CourseColumns: any[] = [
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
		{
			title: '上课日期',
			dataIndex: 'day',
		},
		{
			title: '上课时间',
			dataIndex: 'time',
		},
		{
			title: '课程容量',
			dataIndex: 'capacity',
		},
		{
			title: '已选人数',
			dataIndex: 'choose',
		},
		{
			title: '上课地点',
			dataIndex: 'location',
		},
		{
			title: 'Operation',
			dataIndex: 'op',
			render: (_: any, record: Course) => (
				<Button
					type="primary"
					onClick={() => {
						handleChoose(record.id, stu.id);
					}}
				>
					选课
				</Button>
			),
		},
	];
	return (
		<>
			<Student>
				<InputSearch
					searchButton
					allowClear
					onSearch={(value) => {
						if (value) {
							setUrl(`/api/course/page?name=${value}&pageNum=0&pageSize=1000`);
						}
					}}
					placeholder="Enter keyword to search"
					style={{ width: 350 }}
				/>
				<TableContainer columns={CourseColumns} manage={'course'} url={url} />
			</Student>
		</>
	);
};

export default Courses;

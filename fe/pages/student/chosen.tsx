import { Button, Grid, Message } from '@arco-design/web-react';
import '@arco-design/web-react/dist/css/arco.css';
import Router from 'next/router';
import { useEffect, useState } from 'react';
import Student from '../../components/StuSide';
import TableContainer from '../../components/TableContainer';
import { Course, Student as Stu } from '../../type';
const Row = Grid.Row;
const Col = Grid.Col;

const handleChoose = (id: number, stuId: number) => {
	fetch(`/api/student/cancelCourse?course_id=${id}&student_id=${stuId}`, {
		method: 'POST',
	})
		.then((r) => r.json())
		.then((r) => {
			if (r.code == 200) {
				Message.success('退课成功');
			} else {
				Message.error(r.msg);
			}
		});
};
const Courses = () => {
	const [stu, setStu] = useState<Stu>({} as Stu);
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
					type="secondary"
					status="danger"
					onClick={() => {
						handleChoose(record.id, stu.id);
					}}
				>
					退课
				</Button>
			),
		},
	];
	return (
		<>
			{stu.id ? (
				<Student>
					<TableContainer
						columns={CourseColumns}
						url={`/api/student/getCourses?student_id=${stu.id}`}
					/>
				</Student>
			) : (
				<></>
			)}
		</>
	);
};

export default Courses;

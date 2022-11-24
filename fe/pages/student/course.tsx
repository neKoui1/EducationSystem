import { Grid } from '@arco-design/web-react';
import Router from 'next/router';
import { useEffect, useState } from 'react';
import useSWR from 'swr';
import Side from '../../components/StuSide';
import style from '../../styles/Course.module.css';
import { Course, Student } from '../../type';
const Row = Grid.Row;
const Col = Grid.Col;
interface Props {
	data: Course[];
}
const App = () => {
	const [tea, setTea] = useState<Student | null>(null);
	useEffect(() => {
		const user = localStorage.getItem('student');
		if (user) {
			setTea(JSON.parse(user));
		} else {
			Router.push('/');
		}
	}, []);
	const { data } = useSWR<Props>(
		`/api/student/getCourses?student_id=${tea?.id}`,
		(url) => fetch(url).then((r) => r.json())
	);
	return (
		<Side>
			{['1', '2', '3', '4', '5'].map((item, index) => {
				return (
					<Row key={item}>
						{['周一', '周二', '周三', '周四', '周五', '周六', '周日'].map(
							(day) => (
								<Col key={day} span={3} className={style.box}>
									{data?.data?.map((course) => {
										if (course.day == day && course.time == item) {
											return (
												<>
													<div key={course.id}>{course.name}</div>
													<div key={course.location}>{course.location}</div>
													<div key={course.taught}>{course.taught}</div>
												</>
											);
										}
									})}
								</Col>
							)
						)}
					</Row>
				);
			})}
		</Side>
	);
};

export default App;

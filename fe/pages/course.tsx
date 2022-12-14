import { Grid } from '@arco-design/web-react';
import Side from '../components/Side';
import style from '../styles/Course.module.css';
const Row = Grid.Row;
const Col = Grid.Col;
const data = [
	{
		id: 1,
		name: 'web框架编程',
		credit: 5,
		day: '周一',
		time: '3',
		location: '南401',
		choose: 2,
		capacity: 120,
		taught: 'Lily',
	},
	{
		id: 3,
		name: '数据库系统',
		credit: 4,
		day: '周二',
		time: '3',
		location: null,
		choose: 1,
		capacity: 60,
		taught: 'Lily',
	},
];
const App = () => {
	return (
		<Side>
			{['1', '2', '3', '4', '5'].map((item, index) => {
				return (
					<Row key={item}>
						{['周一', '周二', '周三', '周四', '周五', '周六', '周日'].map(
							(day) => (
								<Col key={day} span={3} className={style.box}>
									{data.map((course) => {
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

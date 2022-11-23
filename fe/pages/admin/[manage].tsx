import {
	Button,
	Form,
	Grid,
	Input,
	Message,
	Modal,
	Radio,
	Select,
} from '@arco-design/web-react';
import '@arco-design/web-react/dist/css/arco.css';
import {
	IconPlusCircle,
	IconToBottom,
	IconToTop,
} from '@arco-design/web-react/icon';
import dynamic from 'next/dynamic';
import { useRouter } from 'next/router';
import React, { useEffect, useRef, useState } from 'react';
import useSWR from 'swr';
import Side from '../../components/Side';
import TableContainer from '../../components/TableContainer';
import { Student } from '../../type';
const Option = Select.Option;
const Row = Grid.Row;
const Col = Grid.Col;
const InputSearch = Input.Search;
const AssignTea = ({
	course_id,
	tea_id,
	setVisible,
}: {
	course_id: number;
	tea_id?: number;
	setVisible: React.Dispatch<React.SetStateAction<boolean>>;
}) => {
	const [id, setId] = useState(0);
	const handleAssign = () => {
		if (id != -1) {
			fetch(
				`/api/teacher/chooseCourse?teacher_id=${id}&course_id=${course_id}`,
				{
					method: 'POST',
				}
			)
				.then((res) => res.json())
				.then((res) => {
					if (res.code == 200) {
						Message.success('分配成功');
						setVisible(false);
					} else {
						Message.error(res.msg);
					}
				});
		} else {
			fetch(
				`
				/api/course/cancel/${course_id}`,
				{
					method: 'POST',
				}
			)
				.then((res) => res.json())
				.then((res) => {
					if (res.code == 200) {
						Message.success('取消成功');
						setVisible(false);
					} else {
						Message.error(res.msg);
					}
				});
		}
	};

	const { data, error } = useSWR<Student[]>('/api/teacher/list', (url) =>
		fetch(url).then((r) => r.json())
	);
	if (error) return <div>failed to load</div>;
	if (!data) return <div>loading...</div>;
	return (
		<>
			<Select style={{ width: 154 }} onChange={(value) => setId(value)}>
				web狂降编程: <Option value={-1}>取消分配</Option>
				{data.map((item) => {
					return (
						<Option key={item.id} value={item.id}>
							{item.name}
						</Option>
					);
				})}
			</Select>{' '}
			<Button type="primary" onClick={handleAssign}>
				分配
			</Button>
		</>
	);
};
const deleteById = (id: string, manage: string): void => {
	fetch(`/api/${manage}/del/${id}`, {
		method: 'DELETE',
	}).then((r) => {
		if (r.ok) {
			Message.success('删除成功');
		} else {
			Message.error('删除失败');
		}
	});
};

const post = (url: string, data: any): void => {
	fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(data),
	}).then((r) =>
		r.json().then((r) => {
			if (r.code == 200) {
				Message.success('操作成功');
			} else {
				Message.error(r.msg);
			}
		})
	);
};

const FileUpload = dynamic(() => import('../../components/Upload'));
const noLabelLayout = {
	wrapperCol: {
		span: 10,
		offset: 5,
	},
};
const FormItem = Form.Item;
const RadioGroup = Radio.Group;
const People = () => {
	return (
		<>
			<FormItem label="姓名" field="name" rules={[{ required: true }]}>
				<Input style={{ width: 270 }} placeholder="please enter your name" />
			</FormItem>
			<FormItem label="密码" field="password" rules={[{ required: true }]}>
				<Input style={{ width: 270 }} placeholder="please enter your post" />
			</FormItem>
			<FormItem label="年龄" field="age" rules={[{ required: true }]}>
				<Input style={{ width: 270 }} placeholder="please enter your post" />
			</FormItem>
			<FormItem label="性别" field="sex" rules={[{ required: true }]}>
				<RadioGroup>
					<Radio value="男">男</Radio>
					<Radio value="女">女</Radio>
				</RadioGroup>
			</FormItem>
			<FormItem field="dept" label="院系" rules={[{ required: true }]}>
				<Input style={{ width: 270 }} placeholder="please enter your post" />
			</FormItem>
		</>
	);
};
const Course = () => {
	return (
		<>
			<FormItem label="课程名称" field="name" rules={[{ required: true }]}>
				<Input style={{ width: 270 }} placeholder="please enter your name" />
			</FormItem>
			<FormItem label="学分" field="credit" rules={[{ required: true }]}>
				<Input
					type="number"
					style={{ width: 270 }}
					placeholder="please enter your post"
				/>
			</FormItem>
			<FormItem label="课程容量" field="capacity" rules={[{ required: true }]}>
				<Input
					type="number"
					style={{ width: 270 }}
					placeholder="please enter your post"
				/>
			</FormItem>
			<FormItem label="上课日期" field="day" rules={[{ required: true }]}>
				<Input style={{ width: 270 }} placeholder="please enter your post" />
			</FormItem>
			<FormItem label="上课时间" field="time" rules={[{ required: true }]}>
				<Input style={{ width: 270 }} placeholder="please enter your post" />
			</FormItem>
			<FormItem label="上课地点" field="location">
				<Input style={{ width: 270 }} placeholder="please enter your post" />
			</FormItem>
		</>
	);
};
function Forms({
	loading1,
	name,
	setLoading1,
	setVisible1,
	fields,
}: {
	loading1: boolean;
	name: string;
	fields: any;
	setLoading1: React.Dispatch<React.SetStateAction<boolean>>;
	setVisible1: React.Dispatch<React.SetStateAction<boolean>>;
}) {
	const formRef = useRef<any>();
	const onValuesChange = (changeValue: any, values: any) => {
		console.log('onValuesChange: ', changeValue, values);
	};
	useEffect(() => {
		if (fields) {
			formRef.current.setFieldsValue({
				...fields,
			});
		} else {
			formRef.current.resetFields();
		}
	}, [fields]);
	return (
		<Form
			ref={formRef}
			autoComplete="off"
			layout={'horizontal'}
			onValuesChange={onValuesChange}
		>
			<FormItem label="姓名" field="id" style={{ display: 'none' }}>
				<Input style={{ width: 270 }} placeholder="please enter your name" />
			</FormItem>
			{name === 'course' ? <Course /> : <People />}
			<FormItem {...noLabelLayout}>
				<Button
					onClick={async () => {
						if (formRef.current) {
							console.log(formRef.current);
							try {
								setLoading1(true);
								await formRef.current.validate();
								console.log(formRef.current.getFieldsValue());
								post(`/api/${name}/save`, formRef.current.getFieldsValue());
								setLoading1(false);
								setVisible1(false);
								Message.success('添加成功');
							} catch (_) {
								console.log(formRef.current.getFieldsError());
								Message.error('校验失败，请检查字段！');
								setLoading1(false);
							}
						}
					}}
					type="primary"
					loading={loading1}
					style={{ marginRight: 24 }}
				>
					Submit
				</Button>
			</FormItem>
		</Form>
	);
}
const App = () => {
	const Buttons = ({ recode }: { recode: any }) => {
		const router = useRouter();
		const [manage, setManage] = useState('course');
		useEffect(() => {
			if (router.isReady) {
				setManage(router.query.manage as string);
			}
		}, [router.isReady, router.query.manage]);
		return (
			<>
				<Button
					onClick={() => {
						setFields(recode);
						setVisible1(true);
						console.log(recode, manage);
					}}
					type="primary"
				>
					修改
				</Button>{' '}
				<Button
					onClick={() => {
						deleteById(recode.id, manage);
					}}
					type="primary"
					status="danger"
				>
					删除
				</Button>{' '}
				{manage === 'course' ? (
					<Button
						onClick={() => {
							setCourse_id(recode.id);
							// setTea_id(recode.teacher_id);
							setVisible2(true);
						}}
						type="secondary"
					>
						分配教师
					</Button>
				) : null}
			</>
		);
	};
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
			render: (_: any, record: any) => <Buttons recode={record} />,
		},
	];
	const StuTeaColumns: any[] = [
		{
			title: 'Name',
			dataIndex: 'name',
		},
		{
			title: '密码',
			dataIndex: 'password',
		},
		{
			title: '年龄',
			dataIndex: 'age',
		},
		{
			title: '性别',
			dataIndex: 'sex',
		},
		{
			title: '院系',
			dataIndex: 'dept',
		},
		{
			title: 'Operation',
			dataIndex: 'op',
			render: (_: any, record: any) => <Buttons recode={record} />,
		},
	];
	const router = useRouter();
	const [name, setName] = useState('course');
	const [columns, setColumns] = useState<any[]>(CourseColumns);
	const [visible1, setVisible1] = useState(false);
	const [visible2, setVisible2] = useState(false);
	const [loading1, setLoading1] = useState(false);
	const [url, setUrl] = useState('');
	const [course_id, setCourse_id] = useState(0);
	const [fields, setFields] = useState<any>({});
	useEffect(() => {
		if (router.isReady) {
			// Code using query
			if (router.query.manage != 'course') {
				setUrl('');
			}
			setName(router.query.manage as string);
			switch (router.query.manage) {
				case 'course':
					setColumns(CourseColumns);
					break;
				case 'student':
					setColumns(StuTeaColumns);
					break;
				case 'teacher':
					setColumns(StuTeaColumns);
					break;
				default:
			}
		}
	}, [router.isReady, router.query.manage]);

	return (
		<Side>
			<Row justify="end">
				<Col span={2}>
					<Button
						type="primary"
						onClick={() => {
							setFields(null);
							setVisible1(true);
						}}
						icon={<IconPlusCircle />}
					>
						添加
					</Button>{' '}
				</Col>
				<Col span={2}>
					{name == 'course' ? (
						<InputSearch
							searchButton
							allowClear
							onSearch={(value) => {
								setUrl(
									`/api/course/page?name=${value}&pageNum=0&pageSize=1000`
								);
							}}
							placeholder="Enter keyword to search"
							style={{ width: 350 }}
						/>
					) : null}
				</Col>
				<Col span={2} offset={16}>
					<FileUpload url={`/api/${name}/import`}>
						<Button type="secondary">
							<IconToBottom />
							导入
						</Button>
					</FileUpload>
				</Col>{' '}
				<Col span={2}>
					<Button type="secondary">
						<a download href={`/api/${name}/export`}>
							<IconToTop />
							导出
						</a>
					</Button>
				</Col>
			</Row>
			<TableContainer columns={columns} manage={name} url={url} />

			<Modal
				title={`修改${
					name == 'course' ? '课程' : name == 'student' ? '学生' : '教师'
				}`}
				visible={visible1}
				footer={null}
				mountOnEnter={false}
				onCancel={() => {
					setVisible1(false);
				}}
			>
				<Forms
					fields={fields}
					loading1={loading1}
					setLoading1={setLoading1}
					setVisible1={setVisible1}
					name={name}
				/>
			</Modal>
			<Modal
				title="分配上课教师"
				footer={null}
				visible={visible2}
				onCancel={() => setVisible2(false)}
			>
				<AssignTea
					// tea_id={tea_id}
					course_id={course_id}
					setVisible={setVisible2}
				/>
			</Modal>
		</Side>
	);
};

export default App;

import { Button, Form, Input, Message } from '@arco-design/web-react';
import { useEffect, useRef, useState } from 'react';
import Stu from '../../components/StuSide';
import { Student } from '../../type';
const noLabelLayout = {
	wrapperCol: {
		span: 10,
		offset: 5,
	},
};
const FormItem = Form.Item;
const Info = () => {
	const formRef = useRef<any>();
	const [modify, setModify] = useState(false);
	const [stu, setStu] = useState<Student | null>(null);
	useEffect(() => {
		const stu = localStorage.getItem('student');
		console.log(stu);
		if (stu) {
			setStu(JSON.parse(stu) as Student);
			formRef.current.setFieldsValue(JSON.parse(stu));
			setModify(false);
		}
	}, []);
	const onValuesChange = (changeValue: any, values: any) => {
		console.log(modify);
		setModify(true);
		console.log('onValuesChange: ', changeValue, values);
	};
	return (
		<Stu>
			<Form
				onValuesChange={onValuesChange}
				ref={formRef}
				layout="horizontal"
				style={{ width: '500px' }}
			>
				<FormItem label="学号" field={'id'}>
					<Input type="number" disabled />
				</FormItem>
				<FormItem label="性别" field={'sex'}>
					<Input type="text" disabled />
				</FormItem>
				<FormItem label="院系" field={'dept'}>
					<Input type="text" disabled />
				</FormItem>
				<FormItem label="年龄" required field={'age'}>
					<Input type="number" />
				</FormItem>
				<FormItem label="姓名" required field={'name'}>
					<Input type="text" />
				</FormItem>
				<FormItem label="密码" required field={'password'}>
					<Input type="password" />
				</FormItem>
				<FormItem {...noLabelLayout}>
					<Button
						onClick={async () => {
							if (formRef.current) {
								console.log(formRef.current);
								try {
									await formRef.current.validate();
									fetch('/api/student/save', {
										method: 'POST',
										body: JSON.stringify(formRef.current.getFieldsValue()),
										headers: {
											'Content-Type': 'application/json',
										},
									})
										.then((res) => res.json())
										.then((res) => {
											if (res.code === '200') {
												Message.success('修改成功');
											} else {
												Message.error('修改失败');
											}
										});
									console.log(formRef.current.getFieldsValue());
								} catch (_) {
									console.log(formRef.current.getFieldsError());
								}
							}
						}}
						type="primary"
						disabled={!modify}
						style={{ marginRight: 24 }}
					>
						Submit
					</Button>
				</FormItem>
			</Form>
		</Stu>
	);
};
export default Info;

import {
	Breadcrumb,
	Grid,
	Layout,
	Menu,
	Message,
} from '@arco-design/web-react';
import { IconCalendar, IconHome } from '@arco-design/web-react/icon';
import Link from 'next/link';
import React from 'react';
const Row = Grid.Row;
const Col = Grid.Col;

const MenuItem = Menu.Item;
const SubMenu = Menu.SubMenu;
const Sider = Layout.Sider;
const Header = Layout.Header;
const Footer = Layout.Footer;
const Content = Layout.Content;

class App extends React.Component<{ children: React.ReactNode }> {
	render() {
		return (
			<Layout className="layout-collapse-demo">
				<Sider
					collapsible
					trigger={null}
					breakpoint="xl"
					style={{ height: '100vh' }}
				>
					<div className="logo" />
					<Menu
						defaultOpenKeys={['1']}
						defaultSelectedKeys={['0_3']}
						onClickMenuItem={(key) =>
							Message.info({
								content: `You select ${key}`,
								showIcon: true,
							})
						}
						style={{ width: '100%', height: '100%' }}
					>
						<Link href={'./teacher'}>
							<MenuItem key="0_1">
								<IconHome />
								教师管理
							</MenuItem>
						</Link>
						<Link href={'./student'}>
							<MenuItem key="0_2">
								<IconCalendar />
								学生管理
							</MenuItem>
						</Link>
						<Link href={'./course'}>
							<MenuItem key="0_3">
								<IconCalendar />
								课程管理
							</MenuItem>
						</Link>
					</Menu>
				</Sider>
				<Layout>
					<Header></Header>
					<Layout style={{ padding: '0 24px' }}>
						<Breadcrumb style={{ margin: '16px 0' }}>
							<Breadcrumb.Item>Home</Breadcrumb.Item>
							<Breadcrumb.Item>List</Breadcrumb.Item>
							<Breadcrumb.Item>App</Breadcrumb.Item>
						</Breadcrumb>
						<Content>{this.props.children}</Content>
						<Footer>Footer</Footer>
					</Layout>
				</Layout>
			</Layout>
		);
	}
}

export default App;

import { Table } from '@arco-design/web-react';

import useSWR from 'swr';
const TableContainer = ({
	columns,
	manage,
	url,
}: {
	columns: any[];
	manage?: string;
	url?: string;
}) => {
	const { data } = useSWR(
		url || `/api/${manage}/list`,
		(url) => fetch(url).then((r) => r.json()),
		{ refreshInterval: 1000 }
	);
	console.log(manage);
	if (!Array.isArray(data)) {
		if (Array.isArray(data?.data)) {
			return <Table columns={columns} data={data?.data} />;
		} else {
			return <Table columns={columns} data={data?.data?.records} />;
		}
	} else {
		return <Table columns={columns} data={data} />;
	}
};

export default TableContainer;

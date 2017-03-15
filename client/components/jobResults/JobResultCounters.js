import React, {Component} from 'react';

export default class JobResultCounters extends Component {

    constructor(props) {
        super(props);

        this.state = {}
    }

    selectRow(el, jid, jobName) {

        let rows = document.querySelectorAll('.job-table__body_row'),
            tr = el.parentElement;

        if (tr.classList.contains('active')) {
            tr.classList.remove('active');
            this.props.hideJobScriptsResult();
        } else {
            for (let i = 0; i < rows.length; i++) {
                if (rows[i].classList.contains('active')) {
                    rows[i].classList.remove('active');
                }
            }

            tr.classList.add('active');
            this.props.showJobScriptResults(jid, jobName);
        }

        this.props.clearFilter();
    }

    render() {
        return <table width='100%' className='mui-table job-table'>
            <tbody>
            <tr>
                <td className='job-table__head mui--text-center' style={{width: '50%'}}>Работа</td>
                <td className='job-table__head mui--text-center' style={{width: '15%'}}>Выполнено</td>
                <td className='job-table__head mui--text-center' style={{width: '15%'}}>Не выполнено</td>
                <td className='job-table__head mui--text-center' style={{width: '20%'}}>Нет соединения</td>
            </tr>
            {this.props.jobResults ? this.props.jobResults.map((el, i) => {
                    return <tr key={i} className='job-table__body_row' title={el.jobName} onClick={(e) => {
                        ::this.selectRow(e.target, el.jid, el.jobName);
                    }}>
                        <td className='job-table__body mui--text-center job-table__body_job-name'>{el.jobName}</td>
                        <td className='job-table__body mui--text-center job-table__body_green'>{el.trueCounts}</td>
                        <td className='job-table__body mui--text-center job-table__body_orange'>{el.falseCounts}</td>
                        <td className='job-table__body mui--text-center job-table__body_red'>{el.notConnectedCounts}</td>
                    </tr>
                }) : null}
            </tbody>
        </table>

    }
}
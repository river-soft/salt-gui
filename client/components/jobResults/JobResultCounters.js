import React, {Component} from 'react';

export default class JobResultCounters extends Component {

    constructor(props) {
        super(props);

        this.state = {}
    }

    selectRow(el, jid, jobName) {

        let rows = document.querySelectorAll('.job-table__body_row'),
            tr = el.parentElement,
            inputs = document.getElementsByTagName('input');

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
        this.props.setExecuteFalse();
        this.props.clearCheckedList();
        this.props.setExecuteErrorFalse();

        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i].type === 'checkbox') {
                inputs[i].checked = false;
            }
        }
    }

    render() {

        let messages = this.props.messages;

        return <table className='mui-table job-table'>
            <tbody>
            <tr>
                <td className='job-table__head mui--text-center' width='40%'>{messages['client.jobresults.job']}</td>
                <td className='job-table__head mui--text-center job-table__body_green' width='15%'
                    title={messages['client.jobresults.tabs.done']}><i className='mi mi-done'></i></td>
                <td className='job-table__head mui--text-center job-table__body_orange' width='15%'
                    title={messages['client.jobresults.tabs.notdone']}><i className='mi mi-close'></i></td>
                <td className='job-table__head mui--text-center job-table__body_red' width='15%'
                    title={messages['client.jobresults.noconnect']}><i className='mi mi-cloud-off'></i></td>
                <td className='job-table__head mui--text-center job-table__body_blue' width='15%'
                    title='Ожидают выполнение'><i className='mi mi-hourglass-empty'></i></td>
            </tr>
            {this.props.jobResults ? this.props.jobResults.map((el, i) => {
                    return <tr key={i} className='job-table__body_row' onClick={(e) => {
                        ::this.selectRow(e.target, el.jid, el.jobName);
                    }}>
                        <td className='job-table__body mui--text-center job-table__body_job-name' title={el.jobName} >{el.jobName}</td>
                        <td className='job-table__body mui--text-center job-table__body_green' title={el.trueCounts} >{el.trueCounts}</td>
                        <td className='job-table__body mui--text-center job-table__body_orange' title={el.falseCounts} >{el.falseCounts}</td>
                        <td className='job-table__body mui--text-center job-table__body_red' title={el.notConnectedCounts} >{el.notConnectedCounts}</td>
                        <td className='job-table__body mui--text-center job-table__body_blue' title={el.waitingCount} >{el.waitingCount}</td>
                    </tr>
                }) : null}
            </tbody>
        </table>
    }
}
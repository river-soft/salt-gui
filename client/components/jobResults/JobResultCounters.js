import React, {Component} from 'react';

export default class JobResultCounters extends Component {

    constructor(props) {
        super(props);

        this.state = {}
    }

    selectRow(el, jid) {

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
            this.props.showJobScriptResults(jid);
        }

        this.props.clearFilter();
    }

    render() {
        return <table width='100%' className='mui-table job-table'>
            <tbody>
            <tr>
                <td className='job-table__head mui--text-center' style={{width: '50%'}}>Job</td>
                <td className='job-table__head mui--text-center' style={{width: '15%'}}>True</td>
                <td className='job-table__head mui--text-center' style={{width: '15%'}}>False</td>
                <td className='job-table__head mui--text-center' style={{width: '20%'}}>No connect</td>
            </tr>
            {this.props.jobResults ? this.props.jobResults.map((el, i) => {
                    return <tr key={i} className='job-table__body_row' onClick={(e) => {
                        ::this.selectRow(e.target, el.jid)
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
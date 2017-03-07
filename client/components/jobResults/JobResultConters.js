import React, {Component} from 'react';

export default class JobResultConters extends Component {


    render() {

        return <table width='100%' className='mui-table job-table'>
            <tr>
                <td className='job-table__head mui--text-center' style={{width: '55%'}}>Job</td>
                <td className='job-table__head mui--text-center' style={{width: '15%'}}>True</td>
                <td className='job-table__head mui--text-center' style={{width: '15%'}}>False</td>
                <td className='job-table__head mui--text-center' style={{width: '15%'}}>No connect</td>
            </tr>
            <tr>
                <td className='job-table__body'>100</td>
                <td className='job-table__body'>100</td>
                <td className='job-table__body'>8000</td>
                <td className='job-table__body'>800</td>
            </tr>
        </table>

    }
}
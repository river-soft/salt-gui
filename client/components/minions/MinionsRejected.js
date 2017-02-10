import React, {Component} from 'react';

export default class MinionsRejected extends Component {

    render() {

        let rejectedMinions = this.props.rejectedMinions;

        return <div className='right-block-list'>
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>NAME</td>
                    </tr>
                    {rejectedMinions ? rejectedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item}</td>
                            </tr>
                        }) : <tr><td>Данных нет</td></tr>}
                    </tbody>
                </table>
            </div>
        </div>
    }
}
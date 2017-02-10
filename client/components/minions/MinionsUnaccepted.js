import React, {Component} from 'react';

export default class MinionsUnaccepted extends Component {

    render() {

        let unacceptedMinions = this.props.unacceptedMinions;

        return <div className='right-block-list'>
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>NAME</td>
                    </tr>
                    {unacceptedMinions ? unacceptedMinions.map((item, index) => {
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
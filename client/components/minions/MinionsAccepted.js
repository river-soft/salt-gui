import React, {Component} from 'react';

export default class MinionsAccepted extends Component {

    render() {

        let acceptedMinions = this.props.acceptedMinions;

        return <div className='right-block-list'>
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>NAME</td>
                        <td className='table__head'>GROUP</td>
                    </tr>

                    {acceptedMinions ? acceptedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item.name}</td>
                                <td>{item.groups}</td>
                            </tr>
                        }) : <tr><td colSpan='2'>Данных нет</td></tr>}
                    </tbody>
                </table>
            </div>
        </div>
    }
}
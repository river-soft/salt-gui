import React, {Component} from 'react';
import Dropdown from 'muicss/lib/react/dropdown';
import DropdownItem from 'muicss/lib/react/dropdown-item';

export default class DateForSelect extends Component {

    constructor(props) {
        super(props);

        this.state = {
            label: 'client.filter.default'
        }
    }

    updateDataByTime(minutes, keyMessage) {

        this.props.loadData(minutes);
        this.setState({label: keyMessage});
        this.props.hideJobScriptsResult();

        let rows = document.querySelectorAll('.job-table__body_row');

        for (let i = 0; i < rows.length; i++) {
            if (rows[i].classList.contains('active')) {
                rows[i].classList.remove('active');
            }
        }
    }

    render() {

        let messages = this.props.messages;

        return <Dropdown className='date-dropdown' color='primary' label={messages[this.state.label]}>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(30, 'client.filter.last.thirty.minutes');
            }}>{messages['client.filter.last.thirty.minutes']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(60, 'client.filter.last.hour');
            }}>{messages['client.filter.last.hour']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(180, 'client.filter.last.three.hours');
            }}>{messages['client.filter.last.three.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(360, 'client.filter.last.six.hours');
            }}>{messages['client.filter.last.six.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(360, 'client.filter.last.twelve.hours');
            }}>{messages['client.filter.last.twelve.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(1440, 'client.filter.last.twenty.four.hours');
            }}>{messages['client.filter.last.twenty.four.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(2880, 'client.filter.last.two.days');
            }}>{messages['client.filter.last.two.days']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={() => {
                ::this.updateDataByTime(10080, 'client.filter.last.seven.days');
            }}>{messages['client.filter.last.seven.days']}</DropdownItem>
        </Dropdown>
    }
}

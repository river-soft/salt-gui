import React, {Component} from 'react';
import Dropdown from 'muicss/lib/react/dropdown';
import DropdownItem from 'muicss/lib/react/dropdown-item';

export default class DateForSelect extends Component {

    constructor(props) {
        super(props);

        this.state = {
            label: this.props.messages['client.filter.default']
        }
    }

    updateDataByTime(minutes, e) {

        this.props.loadData(minutes);
        this.setState({label: e.target.text});
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

        return <Dropdown className='date-dropdown' color='primary' label={this.state.label}>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(30, e);
            }}>{messages['client.filter.last.thirty.minutes']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(60, e);
            }}>{messages['client.filter.last.hour']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(180, e);
            }}>{messages['client.filter.last.three.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(360, e);
            }}>{messages['client.filter.last.six.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(360, e);
            }}>{messages['client.filter.last.twelve.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(1440, e);
            }}>{messages['client.filter.last.twenty.four.hours']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(2880, e);
            }}>{messages['client.filter.last.two.days']}</DropdownItem>
            <DropdownItem className='date-dropdown__item' onClick={(e) => {
                ::this.updateDataByTime(10080, e);
            }}>{messages['client.filter.last.seven.days']}</DropdownItem>
        </Dropdown>
    }
}
